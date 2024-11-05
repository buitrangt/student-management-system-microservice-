package org.aibles.course.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.aibles.course.dto.CourseRequestDTO;
import org.aibles.course.dto.CourseResponseDTO;
import org.aibles.course.entity.Course;
import org.aibles.course.exception.BusinessException;
import org.aibles.course.exception.InstructorCode;
import org.aibles.course.exception.ResponseStatus;
import org.aibles.course.repository.CourseRepository;
import org.aibles.course.service.CourseService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final RestTemplate restTemplate;
    private final String subjectServiceUrl;
    private final String lecturerServiceUrl;

    public CourseServiceImpl(CourseRepository courseRepository, RestTemplate restTemplate,
                             @Value("${subject.service.url}") String subjectServiceUrl,
                             @Value("${lecturer.service.url}") String lecturerServiceUrl) {
        this.courseRepository = courseRepository;
        this.restTemplate = restTemplate;
        this.subjectServiceUrl = subjectServiceUrl;
        this.lecturerServiceUrl = lecturerServiceUrl;
    }

    @Transactional
    @Override
    public CourseResponseDTO create(CourseRequestDTO courseRequestDTO) {
        log.info("(createCourse) Start - courseRequestDTO: {}", courseRequestDTO);

        validateCourseRequest(courseRequestDTO);

        checkExistence(subjectServiceUrl + "/" + courseRequestDTO.getSubjectId(), InstructorCode.SUBJECT_NOT_FOUND);
        checkExistence(lecturerServiceUrl + "/" + courseRequestDTO.getLecturerId(), InstructorCode.LECTURER_NOT_FOUND);

        Course course = new Course();
        course.setCourseName(courseRequestDTO.getCourseName());
        course.setSubjectId(courseRequestDTO.getSubjectId());
        course.setLecturerId(courseRequestDTO.getLecturerId());
        course.setSemester(courseRequestDTO.getSemester());
        course.setAcademicYear(courseRequestDTO.getAcademicYear());

        Course savedCourse = courseRepository.save(course);
        log.info("(createCourse) Successfully created course - courseId: {}", savedCourse.getCourseId());

        return mapToCourseResponseDTO(savedCourse);
    }

    @Transactional
    @Override
    public CourseResponseDTO update(int courseId, CourseRequestDTO courseRequestDTO) {
        log.info("(updateCourse) Start - courseId: {}, courseRequestDTO: {}", courseId, courseRequestDTO);

        validateCourseRequest(courseRequestDTO);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new BusinessException(InstructorCode.COURSE_NOT_FOUND));

        checkExistence(subjectServiceUrl + "/" + courseRequestDTO.getSubjectId(), InstructorCode.SUBJECT_NOT_FOUND);
        checkExistence(lecturerServiceUrl + "/" + courseRequestDTO.getLecturerId(), InstructorCode.LECTURER_NOT_FOUND);

        course.setCourseName(courseRequestDTO.getCourseName());
        course.setSubjectId(courseRequestDTO.getSubjectId());
        course.setLecturerId(courseRequestDTO.getLecturerId());
        course.setSemester(courseRequestDTO.getSemester());
        course.setAcademicYear(courseRequestDTO.getAcademicYear());

        Course updatedCourse = courseRepository.save(course);
        log.info("(updateCourse) Successfully updated course - courseId: {}", updatedCourse.getCourseId());

        return mapToCourseResponseDTO(updatedCourse);
    }

    @Transactional
    @Override
    public void delete(int courseId) {
        log.info("(deleteCourse) Start - courseId: {}", courseId);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new BusinessException(InstructorCode.COURSE_NOT_FOUND));

        courseRepository.delete(course);
        log.info("(deleteCourse) Successfully deleted course - courseId: {}", courseId);
    }

    @Override
    public CourseResponseDTO getById(int courseId) {
        log.info("(getCourseById) Start - courseId: {}", courseId);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new BusinessException(InstructorCode.COURSE_NOT_FOUND));

        log.info("(getCourseById) Successfully retrieved course - courseId: {}", courseId);
        return mapToCourseResponseDTO(course);
    }

    @Override
    public List<CourseResponseDTO> getAll() {
        log.info("(getAllCourses) Start - retrieving all courses");

        List<Course> courses = courseRepository.findAll();
        List<CourseResponseDTO> courseResponseDTOs = courses.stream()
                .map(this::mapToCourseResponseDTO)
                .collect(Collectors.toList());

        log.info("(getAllCourses) Successfully retrieved all courses - total: {}", courseResponseDTOs.size());
        return courseResponseDTOs;
    }

    private void validateCourseRequest(CourseRequestDTO courseRequestDTO) {
        log.info("(validateCourseRequest) Validating request - courseRequestDTO: {}", courseRequestDTO);

        if (!StringUtils.hasText(courseRequestDTO.getCourseName())) {
            log.error("(validateCourseRequest) Course name is empty");
            throw new BusinessException(InstructorCode.INVALID_REQUEST);
        }
        if (courseRequestDTO.getSubjectId() == null) {
            log.error("(validateCourseRequest) Subject ID is null");
            throw new BusinessException(InstructorCode.INVALID_REQUEST);
        }
        if (courseRequestDTO.getLecturerId() == null) {
            log.error("(validateCourseRequest) Lecturer ID is null");
            throw new BusinessException(InstructorCode.INVALID_REQUEST);
        }
        if (!StringUtils.hasText(courseRequestDTO.getSemester())) {
            log.error("(validateCourseRequest) Semester is empty");
            throw new BusinessException(InstructorCode.INVALID_REQUEST);
        }
        if (!StringUtils.hasText(courseRequestDTO.getAcademicYear())) {
            log.error("(validateCourseRequest) Academic year is empty");
            throw new BusinessException(InstructorCode.INVALID_REQUEST);
        }
    }

    private void checkExistence(String url, ResponseStatus errorCode) {
        try {
            restTemplate.getForObject(url, Object.class);
            log.info("(checkExistence) Verified existence for URL: {}", url);
        } catch (HttpClientErrorException.NotFound e) {
            log.error("(checkExistence) Resource not found for URL: {}", url);
            throw new BusinessException(errorCode);
        } catch (Exception e) {
            log.error("(checkExistence) Error verifying existence for URL: {}", url, e);
            throw new BusinessException(InstructorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private CourseResponseDTO mapToCourseResponseDTO(Course course) {
        log.debug("(mapToCourseResponseDTO) Mapping course to DTO - courseId: {}", course.getCourseId());

        CourseResponseDTO courseResponseDTO = new CourseResponseDTO();
        courseResponseDTO.setCourseId(course.getCourseId());
        courseResponseDTO.setCourseName(course.getCourseName());
        courseResponseDTO.setSubjectId(course.getSubjectId());
        courseResponseDTO.setLecturerId(course.getLecturerId());
        courseResponseDTO.setSemester(course.getSemester());
        courseResponseDTO.setAcademicYear(course.getAcademicYear());
        return courseResponseDTO;
    }
}

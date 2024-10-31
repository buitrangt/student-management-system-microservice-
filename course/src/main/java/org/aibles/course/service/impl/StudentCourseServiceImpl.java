package org.aibles.course.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.aibles.course.dto.StudentCourseRequestDTO;
import org.aibles.course.dto.StudentCourseResponseDTO;
import org.aibles.course.entity.StudentCourse;
import org.aibles.course.entity.StudentCourseId;
import org.aibles.course.exception.BusinessException;
import org.aibles.course.exception.InstructorCode;
import org.aibles.course.exception.ResponseStatus;
import org.aibles.course.repository.CourseRepository;
import org.aibles.course.repository.StudentCourseRepository;
import org.aibles.course.service.StudentCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StudentCourseServiceImpl implements StudentCourseService {

    private final StudentCourseRepository studentCourseRepository;
    private final CourseRepository courseRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public StudentCourseServiceImpl(StudentCourseRepository studentCourseRepository,
                                    CourseRepository courseRepository,
                                    RestTemplate restTemplate) {
        this.studentCourseRepository = studentCourseRepository;
        this.courseRepository = courseRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    @Transactional
    public StudentCourseResponseDTO create(StudentCourseRequestDTO studentCourseRequestDTO) {
        log.info("(createStudentCourse) Start - studentCourseRequestDTO: {}", studentCourseRequestDTO);

        // Kiểm tra sự tồn tại của student thông qua RestTemplate
        checkExistence("http://STUDENT-SERVICE/api/v1/students/" + studentCourseRequestDTO.getStudentId(), InstructorCode.STUDENT_NOT_FOUND);

        // Kiểm tra sự tồn tại của course trực tiếp trong cơ sở dữ liệu
        checkCourseExists(studentCourseRequestDTO.getCourseId());

        // Tạo mới một bản ghi StudentCourse
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setStudentId(studentCourseRequestDTO.getStudentId());
        studentCourse.setCourseId(studentCourseRequestDTO.getCourseId());

        StudentCourse savedStudentCourse = studentCourseRepository.save(studentCourse);
        log.info("(createStudentCourse) Successfully created StudentCourse - studentId: {}, courseId: {}",
                savedStudentCourse.getStudentId(), savedStudentCourse.getCourseId());

        return mapToStudentCourseResponseDTO(savedStudentCourse);
    }

    @Override
    public List<StudentCourseResponseDTO> getAll() {
        log.info("(getAllStudentCourses) Start - retrieving all student courses");
        List<StudentCourse> studentCourses = studentCourseRepository.findAll();
        log.info("(getAllStudentCourses) Successfully retrieved all student courses - total: {}", studentCourses.size());
        return studentCourses.stream().map(this::mapToStudentCourseResponseDTO).collect(Collectors.toList());
    }

    @Override
    public StudentCourseResponseDTO getById(int studentId, int courseId) {
        log.info("(getStudentCourseById) Start - studentId: {}, courseId: {}", studentId, courseId);
        StudentCourseId studentCourseId = new StudentCourseId(studentId, courseId);
        StudentCourse studentCourse = studentCourseRepository.findById(studentCourseId)
                .orElseThrow(() -> new BusinessException(InstructorCode.STUDENT_COURSE_NOT_FOUND));
        log.info("(getStudentCourseById) Successfully retrieved StudentCourse - studentId: {}, courseId: {}", studentId, courseId);
        return mapToStudentCourseResponseDTO(studentCourse);
    }

    @Transactional
    @Override
    public void delete(int studentId, int courseId) {
        log.info("(deleteStudentCourse) Start - studentId: {}, courseId: {}", studentId, courseId);
        StudentCourseId studentCourseId = new StudentCourseId(studentId, courseId);
        StudentCourse studentCourse = studentCourseRepository.findById(studentCourseId)
                .orElseThrow(() -> new BusinessException(InstructorCode.STUDENT_COURSE_NOT_FOUND));
        studentCourseRepository.delete(studentCourse);
        log.info("(deleteStudentCourse) Successfully deleted StudentCourse - studentId: {}, courseId: {}", studentId, courseId);
    }

    private void checkExistence(String url, ResponseStatus errorCode) {
        try {
            restTemplate.getForObject(url, Object.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new BusinessException(errorCode);
        } catch (Exception e) {
            throw new BusinessException(InstructorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private void checkCourseExists(Integer courseId) {
        if (!courseRepository.existsById(courseId)) {
            log.error("(checkCourseExists) Course not found - courseId: {}", courseId);
            throw new BusinessException(InstructorCode.COURSE_NOT_FOUND);
        }
        log.info("(checkCourseExists) Course found - courseId: {}", courseId);
    }

    private StudentCourseResponseDTO mapToStudentCourseResponseDTO(StudentCourse studentCourse) {
        StudentCourseResponseDTO studentCourseResponseDTO = new StudentCourseResponseDTO();
        studentCourseResponseDTO.setStudentId(studentCourse.getStudentId());
        studentCourseResponseDTO.setCourseId(studentCourse.getCourseId());
        return studentCourseResponseDTO;
    }
}


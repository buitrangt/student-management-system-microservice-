package org.aibles.grade.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.aibles.grade.dto.GradeRequestDTO;
import org.aibles.grade.dto.GradeResponseDTO;
import org.aibles.grade.entity.Grade;
import org.aibles.grade.entity.GradeId;
import org.aibles.grade.exception.BusinessException;
import org.aibles.grade.exception.InstructorCode;
import org.aibles.grade.exception.ResponseStatus;
import org.aibles.grade.repository.GradeRepository;
import org.aibles.grade.service.GradeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GradeServiceImpl implements GradeService {

    private final GradeRepository gradeRepository;
    private final RestTemplate restTemplate;

    public GradeServiceImpl(GradeRepository gradeRepository, RestTemplate restTemplate) {
        this.gradeRepository = gradeRepository;
        this.restTemplate = restTemplate;
    }

    @Transactional
    @Override
    public GradeResponseDTO create(GradeRequestDTO gradeRequestDTO) {
        log.info("(createGrade) Start - gradeRequestDTO: {}", gradeRequestDTO);

        validateGradeRequest(gradeRequestDTO);

        // Kiểm tra sự tồn tại của student và course thông qua RestTemplate
        checkExistence("http://STUDENT-SERVICE/api/v1/students/" + gradeRequestDTO.getStudentId(), InstructorCode.STUDENT_NOT_FOUND);
        checkExistence("http://COURSE-SERVICE/api/v1/courses/" + gradeRequestDTO.getCourseId(), InstructorCode.COURSE_NOT_FOUND);

        Grade grade = new Grade();
        grade.setStudentId(gradeRequestDTO.getStudentId());
        grade.setCourseId(gradeRequestDTO.getCourseId());
        grade.setScore(gradeRequestDTO.getScore());

        Grade savedGrade = gradeRepository.save(grade);
        log.info("(createGrade) Successfully created grade - studentId: {}, courseId: {}", savedGrade.getStudentId(), savedGrade.getCourseId());

        return mapToGradeResponseDTO(savedGrade);
    }

    @Override
    public List<GradeResponseDTO> getAll() {
        log.info("(getAllGrades) Start - retrieving all grades");

        List<Grade> grades = gradeRepository.findAll();
        List<GradeResponseDTO> gradeResponseDTOs = grades.stream()
                .map(this::mapToGradeResponseDTO)
                .collect(Collectors.toList());

        log.info("(getAllGrades) Successfully retrieved all grades - total: {}", gradeResponseDTOs.size());
        return gradeResponseDTOs;
    }

    @Override
    public GradeResponseDTO getById(Integer studentId, Integer courseId) {
        log.info("(getGradeById) Start - studentId: {}, courseId: {}", studentId, courseId);

        GradeId gradeId = new GradeId(studentId, courseId);
        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new BusinessException(InstructorCode.GRADE_NOT_FOUND));

        log.info("(getGradeById) Successfully retrieved grade - studentId: {}, courseId: {}", studentId, courseId);
        return mapToGradeResponseDTO(grade);
    }

    @Transactional
    @Override
    public GradeResponseDTO update(Integer studentId, Integer courseId, GradeRequestDTO gradeRequestDTO) {
        log.info("(updateGrade) Start - studentId: {}, courseId: {}, gradeRequestDTO: {}", studentId, courseId, gradeRequestDTO);

        validateGradeRequest(gradeRequestDTO);

        GradeId gradeId = new GradeId(studentId, courseId);
        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new BusinessException(InstructorCode.GRADE_NOT_FOUND));

        grade.setScore(gradeRequestDTO.getScore());

        Grade updatedGrade = gradeRepository.save(grade);
        log.info("(updateGrade) Successfully updated grade - studentId: {}, courseId: {}", studentId, courseId);
        return mapToGradeResponseDTO(updatedGrade);
    }

    @Transactional
    @Override
    public void delete(Integer studentId, Integer courseId) {
        log.info("(deleteGrade) Start - studentId: {}, courseId: {}", studentId, courseId);

        GradeId gradeId = new GradeId(studentId, courseId);
        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new BusinessException(InstructorCode.GRADE_NOT_FOUND));

        gradeRepository.delete(grade);
        log.info("(deleteGrade) Successfully deleted grade - studentId: {}, courseId: {}", studentId, courseId);
    }

    private void validateGradeRequest(GradeRequestDTO gradeRequestDTO) {
        log.info("(validateGradeRequest) Validating grade request - gradeRequestDTO: {}", gradeRequestDTO);

        if (gradeRequestDTO.getStudentId() == null || gradeRequestDTO.getCourseId() == null) {
            log.error("(validateGradeRequest) Student ID and Course ID must not be null");
            throw new BusinessException(InstructorCode.INVALID_REQUEST);
        }
        if (gradeRequestDTO.getScore() == null) {
            log.error("(validateGradeRequest) Score must not be null");
            throw new BusinessException(InstructorCode.INVALID_REQUEST);
        }
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


    private GradeResponseDTO mapToGradeResponseDTO(Grade grade) {
        log.debug("(mapToGradeResponseDTO) Mapping grade to DTO - studentId: {}, courseId: {}", grade.getStudentId(), grade.getCourseId());

        GradeResponseDTO gradeResponseDTO = new GradeResponseDTO();
        gradeResponseDTO.setStudentId(grade.getStudentId());
        gradeResponseDTO.setCourseId(grade.getCourseId());
        gradeResponseDTO.setScore(grade.getScore());
        return gradeResponseDTO;
    }
}


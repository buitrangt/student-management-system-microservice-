package org.aibles.subject.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.aibles.subject.dto.SubjectLecturerRequestDTO;
import org.aibles.subject.dto.SubjectLecturerResponseDTO;
import org.aibles.subject.entity.SubjectLecturer;
import org.aibles.subject.entity.SubjectLecturerId;
import org.aibles.subject.exception.BusinessException;
import org.aibles.subject.exception.InstructorCode;
import org.aibles.subject.exception.ResponseStatus;
import org.aibles.subject.repository.SubjectLecturerRepository;
import org.aibles.subject.service.SubjectLecturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SubjectLecturerServiceImpl implements SubjectLecturerService {

    private final SubjectLecturerRepository subjectLecturerRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public SubjectLecturerServiceImpl(SubjectLecturerRepository subjectLecturerRepository, RestTemplate restTemplate) {
        this.subjectLecturerRepository = subjectLecturerRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    @Transactional
    public SubjectLecturerResponseDTO create(SubjectLecturerRequestDTO subjectLecturerRequestDTO) {
        validateSubjectLecturerRequestDTO(subjectLecturerRequestDTO);
        log.info("(createSubjectLecturer) Start - subjectLecturerRequestDTO: {}", subjectLecturerRequestDTO);

        checkExistence("http://SUBJECT-SERVICE/api/v1/subjects/" + subjectLecturerRequestDTO.getSubjectId(), InstructorCode.SUBJECT_NOT_FOUND);
        checkExistence("http://LECTURER-SERVICE/api/v1/lecturers/" + subjectLecturerRequestDTO.getLecturerId(), InstructorCode.LECTURER_NOT_FOUND);

        SubjectLecturer subjectLecturer = new SubjectLecturer();
        subjectLecturer.setSubjectId(subjectLecturerRequestDTO.getSubjectId());
        subjectLecturer.setLecturerId(subjectLecturerRequestDTO.getLecturerId());

        SubjectLecturer savedSubjectLecturer = subjectLecturerRepository.save(subjectLecturer);
        log.info("(createSubjectLecturer) Successfully created subject lecturer - subjectId: {}, lecturerId: {}",
                savedSubjectLecturer.getSubjectId(), savedSubjectLecturer.getLecturerId());

        return mapToSubjectLecturerResponseDTO(savedSubjectLecturer);
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


    @Override
    @Transactional(readOnly = true)
    public List<SubjectLecturerResponseDTO> getAll() {
        log.info("(getAllSubjectLecturers) Start - retrieving all subject lecturers");
        List<SubjectLecturer> subjectLecturers = subjectLecturerRepository.findAll();
        log.info("(getAllSubjectLecturers) Successfully retrieved all subject lecturers - total: {}", subjectLecturers.size());
        return subjectLecturers.stream().map(this::mapToSubjectLecturerResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SubjectLecturerResponseDTO getById(SubjectLecturerId id) {
        validateId(id);
        log.info("(getSubjectLecturerById) Start - subjectId: {}, lecturerId: {}", id.getSubjectId(), id.getLecturerId());
        SubjectLecturer subjectLecturer = subjectLecturerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("(getSubjectLecturerById) Subject Lecturer not found - subjectId: {}, lecturerId: {}",
                            id.getSubjectId(), id.getLecturerId());
                    return new BusinessException(InstructorCode.SUBJECT_LECTURER_NOT_FOUND);
                });
        log.info("(getSubjectLecturerById) Successfully retrieved subject lecturer - subjectId: {}, lecturerId: {}",
                id.getSubjectId(), id.getLecturerId());
        return mapToSubjectLecturerResponseDTO(subjectLecturer);
    }

    @Override
    @Transactional
    public SubjectLecturerResponseDTO update(SubjectLecturerId id, SubjectLecturerRequestDTO subjectLecturerRequestDTO) {
        validateId(id);
        validateSubjectLecturerRequestDTO(subjectLecturerRequestDTO);
        log.info("(updateSubjectLecturer) Start - subjectId: {}, lecturerId: {}", id.getSubjectId(), id.getLecturerId());

        checkExistence("http://SUBJECT-SERVICE/api/v1/subjects/" + subjectLecturerRequestDTO.getSubjectId(), InstructorCode.SUBJECT_NOT_FOUND);
        checkExistence("http://LECTURER-SERVICE/api/v1/lecturers/" + subjectLecturerRequestDTO.getLecturerId(), InstructorCode.LECTURER_NOT_FOUND);

        SubjectLecturer subjectLecturer = subjectLecturerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("(updateSubjectLecturer) Subject Lecturer not found - subjectId: {}, lecturerId: {}",
                            id.getSubjectId(), id.getLecturerId());
                    return new BusinessException(InstructorCode.SUBJECT_LECTURER_NOT_FOUND);
                });

        subjectLecturer.setSubjectId(subjectLecturerRequestDTO.getSubjectId());
        subjectLecturer.setLecturerId(subjectLecturerRequestDTO.getLecturerId());

        SubjectLecturer updatedSubjectLecturer = subjectLecturerRepository.save(subjectLecturer);
        log.info("(updateSubjectLecturer) Successfully updated subject lecturer - subjectId: {}, lecturerId: {}",
                updatedSubjectLecturer.getSubjectId(), updatedSubjectLecturer.getLecturerId());
        return mapToSubjectLecturerResponseDTO(updatedSubjectLecturer);
    }

    @Override
    @Transactional
    public void delete(SubjectLecturerId id) {
        validateId(id);
        log.info("(deleteSubjectLecturer) Start - subjectId: {}, lecturerId: {}", id.getSubjectId(), id.getLecturerId());
        SubjectLecturer subjectLecturer = subjectLecturerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("(deleteSubjectLecturer) Subject Lecturer not found - subjectId: {}, lecturerId: {}",
                            id.getSubjectId(), id.getLecturerId());
                    return new BusinessException(InstructorCode.SUBJECT_LECTURER_NOT_FOUND);
                });
        subjectLecturerRepository.delete(subjectLecturer);
        log.info("(deleteSubjectLecturer) Successfully deleted subject lecturer - subjectId: {}, lecturerId: {}",
                id.getSubjectId(), id.getLecturerId());
    }

    private SubjectLecturerResponseDTO mapToSubjectLecturerResponseDTO(SubjectLecturer subjectLecturer) {
        SubjectLecturerResponseDTO subjectLecturerResponseDTO = new SubjectLecturerResponseDTO();
        subjectLecturerResponseDTO.setSubjectId(subjectLecturer.getSubjectId());
        subjectLecturerResponseDTO.setLecturerId(subjectLecturer.getLecturerId());
        return subjectLecturerResponseDTO;
    }

    private void validateSubjectLecturerRequestDTO(SubjectLecturerRequestDTO dto) {
        if (dto.getSubjectId() == null || dto.getLecturerId() == null) {
            throw new BusinessException(InstructorCode.INVALID_REQUEST);
        }
    }

    private void validateId(SubjectLecturerId id) {
        if (id == null || id.getSubjectId() == null || id.getLecturerId() == null) {
            throw new BusinessException(InstructorCode.INVALID_REQUEST);
        }
    }
}

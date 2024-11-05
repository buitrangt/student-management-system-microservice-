package org.aibles.subject.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.aibles.subject.dto.SubjectRequestDTO;
import org.aibles.subject.dto.SubjectResponseDTO;
import org.aibles.subject.entity.Subject;
import org.aibles.subject.exception.BusinessException;
import org.aibles.subject.exception.InstructorCode;
import org.aibles.subject.repository.SubjectRepository;
import org.aibles.subject.service.SubjectService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final RestTemplate restTemplate;
    private final String departmentServiceUrl;

    public SubjectServiceImpl(SubjectRepository subjectRepository, RestTemplate restTemplate,
                              @Value("${department.service.url}") String departmentServiceUrl) {
        this.subjectRepository = subjectRepository;
        this.restTemplate = restTemplate;
        this.departmentServiceUrl = departmentServiceUrl;
    }

    @Override
    @Transactional
    public SubjectResponseDTO create(SubjectRequestDTO subjectRequestDTO) {
        log.info("(createSubject) Start - subjectRequestDTO: {}", subjectRequestDTO);

        checkDepartmentExists(subjectRequestDTO.getDepartmentId());

        Subject subject = new Subject();
        subject.setSubjectName(subjectRequestDTO.getSubjectName());
        subject.setCredit(subjectRequestDTO.getCredit());
        subject.setDepartmentId(subjectRequestDTO.getDepartmentId());

        Subject savedSubject = subjectRepository.save(subject);
        log.info("(createSubject) Successfully created subject - subjectId: {}", savedSubject.getSubjectId());

        return mapToSubjectResponseDTO(savedSubject);
    }

    private void checkDepartmentExists(Integer departmentId) {
        String departmentUrl = departmentServiceUrl + "/" + departmentId;
        try {
            restTemplate.getForObject(departmentUrl, Object.class);
            log.info("(checkDepartmentExists) Department found - departmentId: {}", departmentId);
        } catch (HttpClientErrorException.NotFound e) {
            log.error("(checkDepartmentExists) Department not found - departmentId: {}", departmentId);
            throw new BusinessException(InstructorCode.DEPARTMENT_NOT_FOUND);
        } catch (Exception e) {
            log.error("(checkDepartmentExists) An error occurred while verifying the department - departmentId: {}", departmentId, e);
            throw new BusinessException(InstructorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubjectResponseDTO> getAll() {
        log.info("(getAllSubjects) Start - retrieving all subjects");
        List<Subject> subjects = subjectRepository.findAll();
        log.info("(getAllSubjects) Successfully retrieved all subjects - total: {}", subjects.size());
        return subjects.stream().map(this::mapToSubjectResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SubjectResponseDTO getById(int subjectId) {
        log.info("(getSubjectById) Start - subjectId: {}", subjectId);
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> {
                    log.error("(getSubjectById) Subject not found - subjectId: {}", subjectId);
                    return new BusinessException(InstructorCode.SUBJECT_NOT_FOUND);
                });
        log.info("(getSubjectById) Successfully retrieved subject - subjectId: {}", subjectId);
        return mapToSubjectResponseDTO(subject);
    }

    @Override
    @Transactional
    public SubjectResponseDTO update(int subjectId, SubjectRequestDTO subjectRequestDTO) {
        log.info("(updateSubject) Start - subjectId: {}", subjectId);

        checkDepartmentExists(subjectRequestDTO.getDepartmentId());

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> {
                    log.error("(updateSubject) Subject not found - subjectId: {}", subjectId);
                    return new BusinessException(InstructorCode.SUBJECT_NOT_FOUND);
                });

        subject.setSubjectName(subjectRequestDTO.getSubjectName());
        subject.setCredit(subjectRequestDTO.getCredit());
        subject.setDepartmentId(subjectRequestDTO.getDepartmentId());

        Subject updatedSubject = subjectRepository.save(subject);
        log.info("(updateSubject) Successfully updated subject - subjectId: {}", updatedSubject.getSubjectId());
        return mapToSubjectResponseDTO(updatedSubject);
    }

    @Override
    @Transactional
    public void delete(int subjectId) {
        log.info("(deleteSubject) Start - subjectId: {}", subjectId);
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> {
                    log.error("(deleteSubject) Subject not found - subjectId: {}", subjectId);
                    return new BusinessException(InstructorCode.SUBJECT_NOT_FOUND);
                });
        subjectRepository.delete(subject);
        log.info("(deleteSubject) Successfully deleted subject - subjectId: {}", subjectId);
    }

    private SubjectResponseDTO mapToSubjectResponseDTO(Subject subject) {
        SubjectResponseDTO subjectResponseDTO = new SubjectResponseDTO();
        subjectResponseDTO.setSubjectId(subject.getSubjectId());
        subjectResponseDTO.setSubjectName(subject.getSubjectName());
        subjectResponseDTO.setCredit(subject.getCredit());
        subjectResponseDTO.setDepartmentId(subject.getDepartmentId());
        return subjectResponseDTO;
    }
}

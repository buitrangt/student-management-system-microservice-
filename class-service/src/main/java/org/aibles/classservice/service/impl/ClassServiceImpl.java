package org.aibles.classservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.aibles.classservice.dto.ClassRequestDTO;
import org.aibles.classservice.dto.ClassResponseDTO;
import org.aibles.classservice.entity.ClassEntity;
import org.aibles.classservice.exception.BusinessException;
import org.aibles.classservice.exception.InstructorCode;
import org.aibles.classservice.exception.ResponseStatus;
import org.aibles.classservice.repository.ClassRepository;
import org.aibles.classservice.service.ClassService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClassServiceImpl implements ClassService {

    private final ClassRepository classRepository;
    private final RestTemplate restTemplate;

    public ClassServiceImpl(ClassRepository classRepository, RestTemplate restTemplate) {
        this.classRepository = classRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    @Transactional
    public ClassResponseDTO create(ClassRequestDTO classRequestDTO) {
        log.info("(create) Start - classRequestDTO: {}", classRequestDTO);

        validateClassRequest(classRequestDTO);

        // Kiểm tra sự tồn tại của department thông qua RestTemplate
        checkExistence("http://DEPARTMENT-SERVICE/api/v1/departments/" + classRequestDTO.getDepartmentId(), InstructorCode.DEPARTMENT_NOT_FOUND);

        ClassEntity classEntity = new ClassEntity();
        classEntity.setClassName(classRequestDTO.getClassName());
        classEntity.setDepartmentId(classRequestDTO.getDepartmentId());

        ClassEntity savedClass = classRepository.save(classEntity);
        log.info("(create) Successfully created class - classId: {}", savedClass.getClassId());

        return mapToClassResponseDTO(savedClass);
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
    @Transactional
    public ClassResponseDTO update(int classId, ClassRequestDTO classRequestDTO) {
        log.info("(update) Start - classId: {}, classRequestDTO: {}", classId, classRequestDTO);

        validateClassRequest(classRequestDTO);

        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new BusinessException(InstructorCode.CLASS_NOT_FOUND));

        // Kiểm tra sự tồn tại của department thông qua RestTemplate
        checkExistence("http://DEPARTMENT-SERVICE/api/v1/departments/" + classRequestDTO.getDepartmentId(), InstructorCode.DEPARTMENT_NOT_FOUND);

        classEntity.setClassName(classRequestDTO.getClassName());
        classEntity.setDepartmentId(classRequestDTO.getDepartmentId());

        ClassEntity updatedClass = classRepository.save(classEntity);
        log.info("(update) Successfully updated class - classId: {}", updatedClass.getClassId());

        return mapToClassResponseDTO(updatedClass);
    }

    @Override
    @Transactional(readOnly = true)
    public ClassResponseDTO getById(int classId) {
        log.info("(getById) Start - classId: {}", classId);

        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new BusinessException(InstructorCode.CLASS_NOT_FOUND));

        log.info("(getById) Successfully retrieved class - classId: {}", classId);
        return mapToClassResponseDTO(classEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClassResponseDTO> getAll() {
        log.info("(getAll) Start - retrieving all classes");

        List<ClassEntity> classEntities = classRepository.findAll();
        List<ClassResponseDTO> classResponseDTOs = classEntities.stream()
                .map(this::mapToClassResponseDTO)
                .collect(Collectors.toList());

        log.info("(getAll) Successfully retrieved all classes - total: {}", classResponseDTOs.size());
        return classResponseDTOs;
    }

    @Override
    @Transactional
    public void delete(int classId) {
        log.info("(delete) Start - classId: {}", classId);

        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new BusinessException(InstructorCode.CLASS_NOT_FOUND));

        classRepository.delete(classEntity);
        log.info("(delete) Successfully deleted class - classId: {}", classId);
    }

    @Override
    public List<ClassResponseDTO> searchByName(String className) {
        log.info("(searchByName) Start - className: {}", className);

        List<ClassEntity> classEntities = classRepository.findByClassNameContainingIgnoreCase(className);
        List<ClassResponseDTO> classResponseDTOs = classEntities.stream()
                .map(this::mapToClassResponseDTO)
                .collect(Collectors.toList());

        log.info("(searchByName) Successfully searched classes - total: {}", classResponseDTOs.size());
        return classResponseDTOs;
    }

    private void validateClassRequest(ClassRequestDTO classRequestDTO) {
        if (classRequestDTO == null) {
            throw new BusinessException(InstructorCode.INVALID_REQUEST);
        }
        if (!StringUtils.hasText(classRequestDTO.getClassName())) {
            throw new BusinessException(InstructorCode.INVALID_REQUEST);
        }
        if (classRequestDTO.getDepartmentId() == null) {
            throw new BusinessException(InstructorCode.INVALID_REQUEST);
        }
    }

    private ClassResponseDTO mapToClassResponseDTO(ClassEntity classEntity) {
        ClassResponseDTO responseDTO = new ClassResponseDTO();
        responseDTO.setClassId(classEntity.getClassId());
        responseDTO.setClassName(classEntity.getClassName());
        responseDTO.setDepartmentId(String.valueOf(classEntity.getDepartmentId()));
        return responseDTO;
    }
}


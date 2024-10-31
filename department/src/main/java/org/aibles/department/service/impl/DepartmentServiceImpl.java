package org.aibles.department.service.impl;

import lombok.extern.slf4j.Slf4j;

import org.aibles.department.dto.DepartmentRequestDTO;
import org.aibles.department.dto.DepartmentResponseDTO;
import org.aibles.department.entity.Department;
import org.aibles.department.exception.BusinessException;
import org.aibles.department.exception.InstructorCode;
import org.aibles.department.repository.DepartmentRepository;
import org.aibles.department.service.DepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    @Override
    public DepartmentResponseDTO create(DepartmentRequestDTO departmentRequestDTO) {
        log.info("(createDepartment) Start - departmentRequestDTO: {}", departmentRequestDTO);

        validateDepartmentRequest(departmentRequestDTO);

        Department department = new Department();
        department.setDepartmentName(departmentRequestDTO.getDepartmentName());

        Department savedDepartment = departmentRepository.save(department);
        log.info("(createDepartment) Successfully created department - departmentId: {}", savedDepartment.getDepartmentId());

        return mapToDepartmentResponseDTO(savedDepartment);
    }

    @Override
    public List<DepartmentResponseDTO> getAll() {
        log.info("(getAllDepartments) Start - retrieving all departments");

        List<Department> departments = departmentRepository.findAll();
        if (departments.isEmpty()) {
            log.warn("(getAllDepartments) No departments found.");
        }

        List<DepartmentResponseDTO> departmentResponseDTOs = departments.stream()
                .map(this::mapToDepartmentResponseDTO)
                .collect(Collectors.toList());

        log.info("(getAllDepartments) Successfully retrieved all departments - total: {}", departmentResponseDTOs.size());
        return departmentResponseDTOs;
    }

    @Override
    public DepartmentResponseDTO getById(int departmentId) {
        log.info("(getDepartmentById) Start - departmentId: {}", departmentId);

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new BusinessException(InstructorCode.DEPARTMENT_NOT_FOUND));

        log.info("(getDepartmentById) Successfully retrieved department - departmentId: {}", departmentId);
        return mapToDepartmentResponseDTO(department);
    }

    @Transactional
    @Override
    public DepartmentResponseDTO update(int departmentId, DepartmentRequestDTO departmentRequestDTO) {
        log.info("(updateDepartment) Start - departmentId: {}, departmentRequestDTO: {}", departmentId, departmentRequestDTO);

        validateDepartmentRequest(departmentRequestDTO);

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new BusinessException(InstructorCode.DEPARTMENT_NOT_FOUND));

        department.setDepartmentName(departmentRequestDTO.getDepartmentName());

        Department updatedDepartment = departmentRepository.save(department);
        log.info("(updateDepartment) Successfully updated department - departmentId: {}", updatedDepartment.getDepartmentId());

        return mapToDepartmentResponseDTO(updatedDepartment);
    }

    @Transactional
    @Override
    public void delete(int departmentId) {
        log.info("(deleteDepartment) Start - departmentId: {}", departmentId);

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new BusinessException(InstructorCode.DEPARTMENT_NOT_FOUND));

        departmentRepository.delete(department);
        log.info("(deleteDepartment) Successfully deleted department - departmentId: {}", departmentId);
    }

    private void validateDepartmentRequest(DepartmentRequestDTO departmentRequestDTO) {
        log.info("(validateDepartmentRequest) Validating request - departmentRequestDTO: {}", departmentRequestDTO);

        if (!StringUtils.hasText(departmentRequestDTO.getDepartmentName())) {
            log.error("(validateDepartmentRequest) Department name is empty");
            throw new BusinessException(InstructorCode.INVALID_REQUEST);
        }
    }

    private DepartmentResponseDTO mapToDepartmentResponseDTO(Department department) {
        log.debug("(mapToDepartmentResponseDTO) Mapping department to DTO - departmentId: {}", department.getDepartmentId());

        DepartmentResponseDTO departmentResponseDTO = new DepartmentResponseDTO();
        departmentResponseDTO.setDepartmentId(department.getDepartmentId());
        departmentResponseDTO.setDepartmentName(department.getDepartmentName());
        return departmentResponseDTO;
    }
}

package org.aibles.department.service;



import org.aibles.department.dto.DepartmentRequestDTO;
import org.aibles.department.dto.DepartmentResponseDTO;

import java.util.List;

public interface DepartmentService {

    DepartmentResponseDTO create(DepartmentRequestDTO departmentRequestDTO);

    List<DepartmentResponseDTO> getAll();

    DepartmentResponseDTO getById(int departmentId);

    DepartmentResponseDTO update(int departmentId, DepartmentRequestDTO departmentRequestDTO);

    void delete(int departmentId);
}


package org.aibles.department.dto;

import lombok.Data;

import java.util.List;


@Data
public class DepartmentListResponse {

    private List<DepartmentResponseDTO> departments;

    public DepartmentListResponse(List<DepartmentResponseDTO> departments) {
        this.departments = departments;
    }


}


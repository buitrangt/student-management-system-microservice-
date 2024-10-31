package org.aibles.grade.dto;

import lombok.Data;

import java.util.List;


@Data
public class GradeListResponse {

    private List<GradeResponseDTO> grades;

    public GradeListResponse(List<GradeResponseDTO> grades) {
        this.grades = grades;
    }


}


package org.aibles.subject.dto;

import lombok.Data;

import java.util.List;


@Data
public class SubjectListResponse {

    private List<SubjectResponseDTO> subjects;

    public SubjectListResponse(List<SubjectResponseDTO> subjects) {
        this.subjects = subjects;
    }


}


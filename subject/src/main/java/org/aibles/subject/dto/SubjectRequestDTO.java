package org.aibles.subject.dto;

import lombok.Data;

@Data
public class SubjectRequestDTO {
    private String subjectName;
    private Integer credit;
    private Integer departmentId;
}

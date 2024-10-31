package org.aibles.auth.utils.dto;


import lombok.Data;

@Data
public class StudentRequest {
    private String userId;
    private String fullName;
    private String email;
    private Long classId;
}


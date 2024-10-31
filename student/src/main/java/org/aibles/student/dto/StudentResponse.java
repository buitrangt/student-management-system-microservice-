package org.aibles.student.dto;

import lombok.Data;

import lombok.Data;

import java.util.Date;

@Data
public class StudentResponse {
    private Integer studentId;
    private String userId;
    private String fullName;
    private Date dateOfBirth;
    private String gender;
    private String address;
    private String email;
    private String phoneNumber;
    private Integer classId;
}

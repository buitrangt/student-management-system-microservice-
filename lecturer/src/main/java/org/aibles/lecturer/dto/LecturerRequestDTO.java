package org.aibles.lecturer.dto;


import lombok.Data;

import java.util.Date;

@Data
public class LecturerRequestDTO {
    private String fullName;
    private Date dateOfBirth;
    private String gender;
    private String email;
    private String phoneNumber;
    private Integer departmentId;
}

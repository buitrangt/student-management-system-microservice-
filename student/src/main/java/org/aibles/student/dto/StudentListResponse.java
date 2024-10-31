package org.aibles.student.dto;

import lombok.Data;

import java.util.List;

@Data
public class StudentListResponse {
    private List<StudentResponse> students;
    private int totalStudents;

    public StudentListResponse(List<StudentResponse> students, int totalStudents) {
        this.students = students;
        this.totalStudents = totalStudents;
    }

}


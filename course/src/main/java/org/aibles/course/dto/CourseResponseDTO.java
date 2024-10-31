package org.aibles.course.dto;


import lombok.Data;

@Data
public class CourseResponseDTO {

    private Integer courseId;
    private String courseName;
    private Integer subjectId;
    private Integer lecturerId;
    private String semester;
    private String academicYear;
}


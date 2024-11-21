package org.aibles.course.dto;


import lombok.Data;

@Data
public class CourseRequestDTO {

    private String courseName;
    private Integer subjectId;
    private Integer lecturerId;
    private String semester;
    private String academicYear;
    private Integer slotsTotal;
}


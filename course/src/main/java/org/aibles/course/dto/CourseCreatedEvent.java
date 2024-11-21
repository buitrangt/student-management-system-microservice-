package org.aibles.course.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseCreatedEvent {
    private Integer courseId;
    private String courseName;
    private Integer subjectId;
    private Integer lecturerId;
    private String semester;
    private String academicYear;
    private Integer slotsTotal; 
}


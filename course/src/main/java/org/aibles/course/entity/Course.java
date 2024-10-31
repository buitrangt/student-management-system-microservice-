package org.aibles.course.entity;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Integer courseId;

    @Column(name = "subject_id", nullable = false)
    private Integer subjectId;

    @Column(name = "lecturer_id", nullable = false)
    private Integer lecturerId;

    @Column(name = "semester", nullable = false)
    private String semester;

    @Column(name = "academic_year", nullable = false)
    private String academicYear;

    @Column(name = "course_name", nullable = false)
    private String courseName;


}

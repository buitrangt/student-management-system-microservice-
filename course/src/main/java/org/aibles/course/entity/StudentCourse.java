package org.aibles.course.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "student_course")
@IdClass(StudentCourseId.class)

public class StudentCourse {

    @Id
    @Column(name = "student_id")
    private Integer studentId;

    @Id
    @Column(name = "course_id")
    private Integer courseId;
}


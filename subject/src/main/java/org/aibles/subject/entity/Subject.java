package org.aibles.subject.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "subject")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private Integer subjectId;

    @Column(name = "subject_name", nullable = false)
    private String subjectName;

    @Column(name = "credit", nullable = false)
    private Integer credit;

    @Column(name = "department_id")
    private Integer departmentId;
}


package org.aibles.classservice.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "class")
@Data
public class ClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    private Integer classId;

    @Column(name = "class_name", nullable = false, length = 50)
    private String className;

    @Column(name = "department_id")
    private Integer departmentId;
}

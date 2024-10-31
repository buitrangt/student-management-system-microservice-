package org.aibles.department.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "department")
@Data
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Integer departmentId;

    @Column(name = "department_name", nullable = false, length = 100)
    private String departmentName;
}

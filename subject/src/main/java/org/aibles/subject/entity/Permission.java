package org.aibles.subject.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "permission")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "resource", nullable = false)
    private String resource;

    @Column(name = "method", nullable = false)
    private String method;

    @Column(name = "description")
    private String description;
}


package org.aibles.student.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_role")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId; // Liên kết với userId trong bảng Student

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}

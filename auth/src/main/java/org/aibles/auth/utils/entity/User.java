package org.aibles.auth.utils.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String userId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name="class_id")
    private Long classId;

    @Column(name="department_id")
    private Long departmentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role = Role.STUDENT;



    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private Long createdAt;

    @Column(name = "created_by", nullable = false)
    @CreatedBy
    private String createdBy;

    @Column(name = "last_updated_at")
    @LastModifiedDate
    private Long lastUpdatedAt;

    @Column(name = "last_updated_by")
    @LastModifiedBy
    private String lastUpdatedBy;
}


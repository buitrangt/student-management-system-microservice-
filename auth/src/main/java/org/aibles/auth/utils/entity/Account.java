package org.aibles.auth.utils.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;


@Entity
@Table(name = "account")
@Data
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String accountId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "created_by", nullable = false)
    @CreatedBy
    private String createdBy;

    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private Long createdAt;

    @Column(name = "last_updated_by")
    @LastModifiedBy
    private String lastUpdatedBy;

    @Column(name = "last_updated_at")
    @LastModifiedDate
    private Long lastUpdatedAt;

    @Column(name = "lock_permanent", nullable = false)
    private boolean lockPermanent = false;



}


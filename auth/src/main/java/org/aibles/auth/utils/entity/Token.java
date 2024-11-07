package org.aibles.auth.utils.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "token")
@Data
public class Token {

    @Id
    private String id;

    private String userId;
    private String token;
    private String status;
    private long createdAt;
    private long expiresAt;

}


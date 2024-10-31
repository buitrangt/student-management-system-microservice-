package org.aibles.gateway.utils.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;


@Entity
@Table(name = "role_scope")
@Data
public class RoleScope {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role;

    private String userId;

    @ManyToOne
    @JoinColumn(name = "scopeId", nullable = false)
    private Scope scope;


}



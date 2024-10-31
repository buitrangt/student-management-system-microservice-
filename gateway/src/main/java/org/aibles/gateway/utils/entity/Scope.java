package org.aibles.gateway.utils.entity;


import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.annotation.*;


@Entity
@Table(name = "scope")
@Data
public class Scope {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pattern;

    private Long serviceId;

    private Boolean isDelete;
    private String method;

}



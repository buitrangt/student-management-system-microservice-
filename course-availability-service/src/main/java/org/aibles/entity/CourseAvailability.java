package org.aibles.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "course_availability")
public class CourseAvailability {

    @Id
    @Column(name = "course_id")
    private Integer courseId;

    @Column(name = "slots_total", nullable = false)
    private Integer slotsTotal;

    @Column(name = "slots_available", nullable = false)
    private Integer slotsAvailable;

    @Column(name = "last_updated")
    private Date lastUpdated;
}


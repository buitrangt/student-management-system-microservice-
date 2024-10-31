package org.aibles.subject.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "subject_lecturer")
@IdClass(SubjectLecturerId.class)
public class SubjectLecturer {

    @Id
    @Column(name = "subject_id")
    private Integer subjectId;

    @Id
    @Column(name = "lecturer_id")
    private Integer lecturerId;
}


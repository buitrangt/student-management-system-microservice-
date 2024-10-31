package org.aibles.subject.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectLecturerId implements Serializable {
    private Integer subjectId;
    private Integer lecturerId;
}


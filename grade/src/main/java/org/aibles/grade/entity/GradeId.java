package org.aibles.grade.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GradeId implements Serializable {

    private Integer studentId;
    private Integer courseId;
}



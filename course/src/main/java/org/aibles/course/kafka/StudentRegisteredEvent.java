package org.aibles.course.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentRegisteredEvent {
    private Integer courseId;
    private Integer studentId;
}

package org.aibles.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aibles.dto.StudentRegisteredEvent;
import org.aibles.service.CourseAvailabilityService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseAvailabilityListener {

    private final CourseAvailabilityService courseAvailabilityService;

    @KafkaListener(topics = "student-registered", groupId = "course-availability-group")
    public void handleStudentRegisteredEvent(StudentRegisteredEvent event) {
        log.info("(handleStudentRegisteredEvent) Received event: {}", event);

        courseAvailabilityService.decreaseSlots(event.getCourseId());
    }
}


package org.aibles.service.impl;


import org.aibles.entity.CourseAvailability;
import org.aibles.exception.BusinessException;
import org.aibles.exception.InstructorCode;
import org.aibles.repository.CourseAvailabilityRepository;
import org.aibles.service.CourseAvailabilityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service
public class CourseAvailabilityServiceImpl implements CourseAvailabilityService {

    private final CourseAvailabilityRepository repository;

    public CourseAvailabilityServiceImpl(CourseAvailabilityRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public CourseAvailability getAvailability(Integer courseId) {
        return repository.findById(courseId)
                .orElseThrow(() -> new BusinessException(InstructorCode.COURSE_AVAILABILITY_NOT_FOUND));
    }

    @Override
    @Transactional
    public void decreaseSlots(Integer courseId) {
        CourseAvailability availability = getAvailability(courseId);

        if (availability.getSlotsAvailable() <= 0) {
            throw new BusinessException(InstructorCode.NO_AVAILABLE_SLOTS);
        }

        availability.setSlotsAvailable(availability.getSlotsAvailable() - 1);
        availability.setLastUpdated(new Date());
        repository.save(availability);
    }

    @Override
    @Transactional
    public void increaseSlots(Integer courseId) {
        CourseAvailability availability = getAvailability(courseId);

        if (availability.getSlotsAvailable() < availability.getSlotsTotal()) {
            availability.setSlotsAvailable(availability.getSlotsAvailable() + 1);
            availability.setLastUpdated(new Date());
            repository.save(availability);
        } else {
            throw new BusinessException(InstructorCode.SLOTS_EXCEED_TOTAL);
        }
    }

    @Override
    @Transactional
    public CourseAvailability createAvailability(CourseAvailability courseAvailability) {
        if (repository.existsById(courseAvailability.getCourseId())) {
            throw new BusinessException(InstructorCode.AVAILABILITY_ALREADY_EXISTS);
        }
        courseAvailability.setLastUpdated(new Date());
        return repository.save(courseAvailability);
    }
}


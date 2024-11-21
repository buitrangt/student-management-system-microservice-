package org.aibles.repository;


import org.aibles.entity.CourseAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseAvailabilityRepository extends JpaRepository<CourseAvailability, Integer> {
}

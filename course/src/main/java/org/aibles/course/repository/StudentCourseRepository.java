package org.aibles.course.repository;


import org.aibles.course.entity.StudentCourse;
import org.aibles.course.entity.StudentCourseId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, StudentCourseId> {
}


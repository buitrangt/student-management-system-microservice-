package org.aibles.subject.repository;


import org.aibles.subject.entity.SubjectLecturer;
import org.aibles.subject.entity.SubjectLecturerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectLecturerRepository extends JpaRepository<SubjectLecturer, SubjectLecturerId> {
}


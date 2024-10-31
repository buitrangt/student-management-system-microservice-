package org.aibles.grade.repository;


import org.aibles.grade.entity.Grade;
import org.aibles.grade.entity.GradeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeRepository extends JpaRepository<Grade, GradeId> {
}


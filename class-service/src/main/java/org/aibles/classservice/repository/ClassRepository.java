package org.aibles.classservice.repository;

import org.aibles.classservice.entity.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, Integer> {

    List<ClassEntity> findByClassNameContainingIgnoreCase(String className);
}



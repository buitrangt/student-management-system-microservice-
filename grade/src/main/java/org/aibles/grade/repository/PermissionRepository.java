package org.aibles.grade.repository;

import org.aibles.grade.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Permission findByResourceAndMethod(String resource, String method);
}
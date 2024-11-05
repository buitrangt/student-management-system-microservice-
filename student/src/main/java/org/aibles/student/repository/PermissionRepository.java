package org.aibles.student.repository;

import org.aibles.student.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Permission findByResourceAndMethod(String resource, String method);
}
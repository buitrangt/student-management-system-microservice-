package org.aibles.department.repository;

import org.aibles.department.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Permission findByResourceAndMethod(String resource, String method);
}
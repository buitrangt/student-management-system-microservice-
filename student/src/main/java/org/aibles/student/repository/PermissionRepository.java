package org.aibles.student.repository;

import org.aibles.student.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import reactor.core.publisher.Mono;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Permission findByResourceAndMethod(String resource, String method);
    Mono<Permission> findByRoleIdAndResourceAndMethod(Long roleId, String resource, String method);
}

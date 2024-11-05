package org.aibles.attendance.repository;
import org.aibles.attendance.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    boolean existsByRoleIdAndPermissionId(Long roleId, Long permissionId);
}

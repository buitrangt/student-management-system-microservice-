package org.aibles.student.service;

import org.aibles.student.entity.RolePermission;

import java.util.List;

public interface RolePermissionService {
    List<RolePermission> findPermissionsByRoleId(Long roleId);
    RolePermission saveRolePermission(RolePermission rolePermission);
}

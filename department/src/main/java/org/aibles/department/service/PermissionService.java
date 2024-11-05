package org.aibles.department.service;


import org.aibles.department.entity.Permission;

public interface PermissionService {
    Permission findPermission(String resource, String method);
    Permission savePermission(Permission permission);

    boolean hasAccess(Long roleId, String resource, String method);

    Long findRoleIdByRoleName(String roleName);

}

package org.aibles.course.service;


import org.aibles.course.entity.Permission;

public interface PermissionService {
    Permission findPermission(String resource, String method);
    Permission savePermission(Permission permission);

    boolean hasAccess(Long roleId, String resource, String method);

    Long findRoleIdByRoleName(String roleName);

}

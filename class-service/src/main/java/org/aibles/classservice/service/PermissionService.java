package org.aibles.classservice.service;


import org.aibles.classservice.entity.Permission;

public interface PermissionService {
    Permission findPermission(String resource, String method);
    Permission savePermission(Permission permission);

    boolean hasAccess(Long roleId, String resource, String method);

    Long findRoleIdByRoleName(String roleName);

}

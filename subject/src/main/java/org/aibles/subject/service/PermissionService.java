package org.aibles.subject.service;


import org.aibles.subject.entity.Permission;

public interface PermissionService {
    Permission findPermission(String resource, String method);
    Permission savePermission(Permission permission);

    boolean hasAccess(Long roleId, String resource, String method);

    Long findRoleIdByRoleName(String roleName);

}

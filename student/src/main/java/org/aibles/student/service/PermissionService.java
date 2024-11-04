package org.aibles.student.service;

import org.aibles.student.entity.Permission;
import reactor.core.publisher.Mono;

public interface PermissionService {
    Permission findPermission(String resource, String method);
    Permission savePermission(Permission permission);

    boolean hasAccess(Long roleId, String resource, String method);

    Long findRoleIdByRoleName(String roleName);

}

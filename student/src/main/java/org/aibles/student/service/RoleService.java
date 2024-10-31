package org.aibles.student.service;

import org.aibles.student.entity.Role;

public interface RoleService {
    Role findRoleByName(String name);
    Role saveRole(Role role);
}

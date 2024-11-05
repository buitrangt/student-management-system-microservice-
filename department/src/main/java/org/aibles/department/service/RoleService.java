package org.aibles.department.service;


import org.aibles.department.entity.Role;

public interface RoleService {
    Role findRoleByName(String name);
    Role saveRole(Role role);
}

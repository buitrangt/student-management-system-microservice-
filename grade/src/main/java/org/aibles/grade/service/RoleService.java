package org.aibles.grade.service;


import org.aibles.grade.entity.Role;

public interface RoleService {
    Role findRoleByName(String name);
    Role saveRole(Role role);
}

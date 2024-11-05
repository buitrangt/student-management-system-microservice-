package org.aibles.lecturer.service;


import org.aibles.lecturer.entity.Role;

public interface RoleService {
    Role findRoleByName(String name);
    Role saveRole(Role role);
}

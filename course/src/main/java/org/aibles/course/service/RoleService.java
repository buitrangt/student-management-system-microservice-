package org.aibles.course.service;


import org.aibles.course.entity.Role;

public interface RoleService {
    Role findRoleByName(String name);
    Role saveRole(Role role);
}

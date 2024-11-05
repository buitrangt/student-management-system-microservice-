package org.aibles.attendance.service;


import org.aibles.attendance.entity.Role;

public interface RoleService {
    Role findRoleByName(String name);
    Role saveRole(Role role);
}

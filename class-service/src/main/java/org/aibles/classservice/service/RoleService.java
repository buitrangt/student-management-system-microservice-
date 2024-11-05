package org.aibles.classservice.service;


import org.aibles.classservice.entity.Role;

public interface RoleService {
    Role findRoleByName(String name);
    Role saveRole(Role role);
}

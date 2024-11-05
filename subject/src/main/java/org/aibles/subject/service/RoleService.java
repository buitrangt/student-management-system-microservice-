package org.aibles.subject.service;


import org.aibles.subject.entity.Role;

public interface RoleService {
    Role findRoleByName(String name);
    Role saveRole(Role role);
}

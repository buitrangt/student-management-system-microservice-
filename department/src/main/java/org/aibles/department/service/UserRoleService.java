package org.aibles.department.service;


import org.aibles.department.entity.UserRole;

import java.util.List;

public interface UserRoleService {
    List<UserRole> findRolesByUserId(String userId);
    UserRole saveUserRole(UserRole userRole);
}

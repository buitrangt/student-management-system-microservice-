package org.aibles.grade.service;


import org.aibles.grade.entity.UserRole;

import java.util.List;

public interface UserRoleService {
    List<UserRole> findRolesByUserId(String userId);
    UserRole saveUserRole(UserRole userRole);
}

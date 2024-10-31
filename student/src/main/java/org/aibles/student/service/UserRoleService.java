package org.aibles.student.service;

import org.aibles.student.entity.UserRole;

import java.util.List;

public interface UserRoleService {
    List<UserRole> findRolesByUserId(String userId);
    UserRole saveUserRole(UserRole userRole);
}

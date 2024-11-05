package org.aibles.attendance.service;


import org.aibles.attendance.entity.UserRole;

import java.util.List;

public interface UserRoleService {
    List<UserRole> findRolesByUserId(String userId);
    UserRole saveUserRole(UserRole userRole);
}

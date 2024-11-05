package org.aibles.course.service;


import org.aibles.course.entity.UserRole;

import java.util.List;

public interface UserRoleService {
    List<UserRole> findRolesByUserId(String userId);
    UserRole saveUserRole(UserRole userRole);
}

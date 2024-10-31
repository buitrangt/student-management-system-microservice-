package org.aibles.student.service.impl;

import org.aibles.student.entity.RolePermission;
import org.aibles.student.repository.RolePermissionRepository;
import org.aibles.student.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RolePermissionServiceImpl implements RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;

    @Autowired
    public RolePermissionServiceImpl(RolePermissionRepository rolePermissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Override
    public List<RolePermission> findPermissionsByRoleId(Long roleId) {
        return rolePermissionRepository.findByRoleId(roleId);
    }

    @Override
    public RolePermission saveRolePermission(RolePermission rolePermission) {
        return rolePermissionRepository.save(rolePermission);
    }
}

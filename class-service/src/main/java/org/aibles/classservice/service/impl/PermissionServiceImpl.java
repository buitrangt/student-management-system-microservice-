package org.aibles.classservice.service.impl;

import org.aibles.classservice.entity.Permission;
import org.aibles.classservice.entity.Role;
import org.aibles.classservice.repository.PermissionRepository;
import org.aibles.classservice.repository.RolePermissionRepository;
import org.aibles.classservice.repository.RoleRepository;
import org.aibles.classservice.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public PermissionServiceImpl(PermissionRepository permissionRepository, RolePermissionRepository rolePermissionRepository,RoleRepository roleRepository) {
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Permission findPermission(String resource, String method) {
        return permissionRepository.findByResourceAndMethod(resource, method);
    }

    @Override
    public Permission savePermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public boolean hasAccess(Long roleId, String resource, String method) {
        Permission permission = permissionRepository.findByResourceAndMethod(resource, method);

        return permission != null && rolePermissionRepository.existsByRoleIdAndPermissionId(roleId, permission.getId());
    }

    @Override
    public Long findRoleIdByRoleName(String roleName) {
        Role role = roleRepository.findByName(roleName);
        return role != null ? role.getId() : null;
    }

}

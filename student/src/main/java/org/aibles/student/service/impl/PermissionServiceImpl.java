package org.aibles.student.service.impl;

import org.aibles.student.entity.Permission;
import org.aibles.student.repository.PermissionRepository;
import org.aibles.student.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    @Autowired
    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
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
    public Mono<Permission> findPermissionByRoleAndResource(Long roleId, String resource, String method) {
        return permissionRepository.findByRoleIdAndResourceAndMethod(roleId, resource, method);
    }
}

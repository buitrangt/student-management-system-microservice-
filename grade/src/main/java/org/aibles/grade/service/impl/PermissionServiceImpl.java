package org.aibles.grade.service.impl;
import org.aibles.grade.entity.Permission;
import org.aibles.grade.entity.Role;
import org.aibles.grade.repository.PermissionRepository;
import org.aibles.grade.repository.RolePermissionRepository;
import org.aibles.grade.repository.RoleRepository;
import org.aibles.grade.service.PermissionService;
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
        // Tìm Permission dựa trên resource và method
        Permission permission = permissionRepository.findByResourceAndMethod(resource, method);

        // Kiểm tra xem roleId có quyền truy cập permission này không
        return permission != null && rolePermissionRepository.existsByRoleIdAndPermissionId(roleId, permission.getId());
    }

    @Override
    public Long findRoleIdByRoleName(String roleName) {
        Role role = roleRepository.findByName(roleName);
        return role != null ? role.getId() : null;
    }

}

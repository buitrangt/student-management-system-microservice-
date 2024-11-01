package org.aibles.student.service.impl;
import org.aibles.student.entity.Permission;
import org.aibles.student.repository.PermissionRepository;
import org.aibles.student.repository.RolePermissionRepository;
import org.aibles.student.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    @Autowired
    public PermissionServiceImpl(PermissionRepository permissionRepository, RolePermissionRepository rolePermissionRepository) {
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
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
}

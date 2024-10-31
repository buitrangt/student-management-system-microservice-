package org.aibles.student.service.impl;

import org.aibles.student.entity.Role;
import org.aibles.student.repository.RoleRepository;
import org.aibles.student.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }
}

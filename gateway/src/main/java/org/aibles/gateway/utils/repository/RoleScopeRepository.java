package org.aibles.gateway.utils.repository;

import org.aibles.gateway.utils.entity.RoleScope;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleScopeRepository extends JpaRepository<RoleScope, Long> {
    boolean existsByUserIdAndScopeId(String userId, Long scopeId);
}


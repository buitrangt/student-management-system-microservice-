package org.aibles.gateway.utils.repository;

import org.aibles.gateway.utils.entity.Scope;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScopeRepository extends JpaRepository<Scope, Long> {
    Scope findByPatternAndMethod(String pattern, String method);
}


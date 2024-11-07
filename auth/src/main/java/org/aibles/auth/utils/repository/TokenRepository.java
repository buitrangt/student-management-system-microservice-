package org.aibles.auth.utils.repository;

import org.aibles.auth.utils.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);
    List<Token> findAllByUserIdAndStatus(String userId, String status);
    void deleteByToken(String token);
    void deleteAllByUserId(String userId);
}

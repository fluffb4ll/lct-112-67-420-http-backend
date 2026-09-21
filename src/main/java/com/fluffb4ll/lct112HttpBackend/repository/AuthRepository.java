package com.fluffb4ll.lct112HttpBackend.repository;

import com.fluffb4ll.lct112HttpBackend.entity.AuthTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthRepository extends JpaRepository<AuthTokenEntity, UUID> {
    Optional<AuthTokenEntity> findTokenByUserId(UUID userId);
    Optional<AuthTokenEntity> findById(UUID id);
    Optional<AuthTokenEntity> findByToken(UUID token);
    void removeAuthTokenEntityById(UUID id);
}

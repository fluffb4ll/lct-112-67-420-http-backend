package com.fluffb4ll.lct112HttpBackend.repository;

import com.fluffb4ll.lct112HttpBackend.entity.RoleEntity;
import com.fluffb4ll.lct112HttpBackend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findById(UUID id);
    @Query("SELECT u FROM UserEntity u " +
            "JOIN FETCH u.role " +
            "LEFT JOIN FETCH u.department " +
            "LEFT JOIN FETCH u.studyGroups " +
            "WHERE u.id = :id")
    Optional<UserEntity> findByIdForLogin(@Param("id") UUID id);
    boolean existsByRole(RoleEntity role);
}

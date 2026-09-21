package com.fluffb4ll.lct112HttpBackend.repository;

import com.fluffb4ll.lct112HttpBackend.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<RoleEntity, Integer> {
    Optional<RoleEntity> findById(int id);
    Optional<String> findNameById(int id);
    Optional<RoleEntity> findByName(String name);
}

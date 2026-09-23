package com.fluffb4ll.lct112HttpBackend.repository;

import com.fluffb4ll.lct112HttpBackend.entity.StudyGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudyGroupRepository extends JpaRepository<StudyGroupEntity, UUID> {
    Optional<StudyGroupEntity> findByName(String name);
}

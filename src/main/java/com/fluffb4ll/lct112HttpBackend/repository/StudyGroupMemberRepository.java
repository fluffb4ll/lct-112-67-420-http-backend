package com.fluffb4ll.lct112HttpBackend.repository;

import com.fluffb4ll.lct112HttpBackend.entity.StudyGroupMemberEntity;
import com.fluffb4ll.lct112HttpBackend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface StudyGroupMemberRepository extends JpaRepository<StudyGroupMemberEntity, UUID> {

    @Query("SELECT m.user FROM StudyGroupMemberEntity m " +
            "JOIN m.user u " +
            "LEFT JOIN FETCH u.role " +
            "LEFT JOIN FETCH u.department " +
            "WHERE m.group.id = :groupId")
    List<UserEntity> findUsersByGroupId(@Param("groupId") UUID groupId);

    void deleteByGroupIdAndUserId(UUID groupId, UUID userId);

    boolean existsByGroupIdAndUserId(UUID groupId, UUID userId);
}
package com.fluffb4ll.lct112HttpBackend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Getter
@Entity
@Table(
        name = "study_group_members",
        schema = "iam",
        uniqueConstraints = @UniqueConstraint(columnNames = {"group_id", "student_id"})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyGroupMemberEntity {
    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private StudyGroupEntity group;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private UserEntity user;

    @Column(name = "joined_at", nullable = false)
    private OffsetDateTime joinedAt;

    public StudyGroupMemberEntity(StudyGroupEntity group, UserEntity user) {
        this.group = group;
        this.user = user;
        this.joinedAt = OffsetDateTime.now();
    }
}
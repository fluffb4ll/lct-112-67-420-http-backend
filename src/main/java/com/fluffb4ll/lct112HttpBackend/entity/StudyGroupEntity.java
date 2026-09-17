package com.fluffb4ll.lct112HttpBackend.entity;

import com.fluffb4ll.lct112HttpBackend.util.IdGeneratorUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "study_groups", schema = "iam")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyGroupEntity {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Setter
    @Column(name = "name", nullable = false, unique = true, length = 150)
    private String name;

    @Setter
    @Column(name = "teacher_id", nullable = false)
    private UUID teacherId;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    public StudyGroupEntity(String name, UUID teacherId) {
        id = IdGeneratorUtil.generateId();
        this.name = name;
        this.teacherId = teacherId;
        createdAt = OffsetDateTime.now();
    }
}

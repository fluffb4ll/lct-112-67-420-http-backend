package com.fluffb4ll.lct112HttpBackend.entity;

import com.fluffb4ll.lct112HttpBackend.util.IdGeneratorUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "users", schema = "iam")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Setter
    @Column(name = "username", length = 64, nullable = false, unique = true)
    private String username;

    @Setter
    @Column(name = "password_hash", length = 60, nullable = false)
    private String passwordHash;

    @Setter
    @Column(name = "full_name", length = 150, nullable = false)
    private String fullName;

    @Column(name = "role_id", nullable = false)
    private int roleId;

    @Setter
    @Column(name = "department_id")
    private UUID departmentId;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Setter
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Setter
    @Column(name = "is_active")
    private boolean active;

    public UserEntity(String username, String passwordHash, String fullName, int roleId) {
        id = IdGeneratorUtil.generateId();
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.roleId = roleId;
        this.active = true;
        createdAt = OffsetDateTime.now();
    }
}

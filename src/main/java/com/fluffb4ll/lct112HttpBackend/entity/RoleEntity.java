package com.fluffb4ll.lct112HttpBackend.entity;

import com.fluffb4ll.lct112HttpBackend.model.enums.Permissions;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Entity
@Table(name = "roles", schema = "iam")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Setter
    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "permissions", columnDefinition = "integer[]")
    private Integer[] permissions;

    public Set<String> getPermissionsAsSet() {
        if (permissions == null) return Collections.emptySet();
        return Arrays.stream(permissions)
                .map(Permissions::fromIndex)
                .map(Permissions::name)
                .collect(Collectors.toSet());
    }
//
//    public boolean addPermission(int permission) {
//        return permissions.add(Permissions.fromIndex(permission).toString());
//    }
//
//    public boolean removePermission(int permission) {
//        return permissions.remove(Permissions.fromIndex(permission).toString());
//    }
}

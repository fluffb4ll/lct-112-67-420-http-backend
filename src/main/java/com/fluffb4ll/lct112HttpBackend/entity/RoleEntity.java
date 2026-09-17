package com.fluffb4ll.lct112HttpBackend.entity;

import com.fluffb4ll.lct112HttpBackend.model.enums.Permissions;
import com.fluffb4ll.lct112HttpBackend.util.PermissionsArrayToSetConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

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

    @Convert(converter = PermissionsArrayToSetConverter.class)
    @Column(name = "permissions", columnDefinition = "integer[]")
    private Set<String> permissions;

    public boolean addPermission(int permission) {
        return permissions.add(Permissions.fromIndex(permission).toString());
    }

    public boolean removePermission(int permission) {
        return permissions.remove(Permissions.fromIndex(permission).toString());
    }
}

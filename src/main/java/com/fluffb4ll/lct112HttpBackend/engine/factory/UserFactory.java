package com.fluffb4ll.lct112HttpBackend.engine.factory;

import com.fluffb4ll.lct112HttpBackend.entity.RoleEntity;
import com.fluffb4ll.lct112HttpBackend.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFactory {
    private final PasswordEncoder passEncoder;

    public UserEntity createUser(
            String username,
            String password,
            String fullName,
            RoleEntity role
    ) {
        return new UserEntity(
            username,
            password,
            fullName,
            role
        );
    }

    public UserEntity createUserWithPasswordHashing(
            String username,
            String password,
            String fullName,
            RoleEntity role
    ) {
        String passwordHash = passEncoder.encode(password);
        return createUser(
                username,
                passwordHash,
                fullName,
                role
        );
    }
}

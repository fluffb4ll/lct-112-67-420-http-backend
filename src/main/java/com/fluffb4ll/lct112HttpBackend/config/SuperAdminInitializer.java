package com.fluffb4ll.lct112HttpBackend.config;

import com.fluffb4ll.lct112HttpBackend.engine.factory.UserFactory;
import com.fluffb4ll.lct112HttpBackend.entity.RoleEntity;
import com.fluffb4ll.lct112HttpBackend.entity.UserEntity;
import com.fluffb4ll.lct112HttpBackend.repository.RolesRepository;
import com.fluffb4ll.lct112HttpBackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class SuperAdminInitializer implements ApplicationRunner {
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserFactory userFactory;

    @Value("${app.security.init.admin-password:}")
    private String configuredPassword;

    @Value("${app.security.init.admin-username:admin}")
    private String adminUsername;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        RoleEntity adminRole = rolesRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new IllegalStateException("ROLE_ADMIN not found in DB"));

        boolean adminExists = userRepository.existsByRole(adminRole);

        if (adminExists)
            return;

        log.warn("No admins found - initializing default superadmin profile...");

        boolean isGenerated = false;
        String rawPassword = configuredPassword;
        if (rawPassword == null || rawPassword.isBlank()) {
            rawPassword = generateSecurePassword();
            isGenerated = true;
        }

        String passwordHash = passwordEncoder.encode(rawPassword);
        UserEntity superAdmin = userFactory.createUser(
                adminUsername,
                passwordHash,
                "SuperAdmin",
                adminRole
        );

        superAdmin.setMustChangePassword(true);
        userRepository.save(superAdmin);

        log.info("==================================================================");
        log.info("Superadmin created successfully!");
        log.info("Username: {}", adminUsername);
        if (isGenerated) {
            log.info("Generated temporary password: {}", rawPassword);
            log.info("MAKE SURE TO SAVE THIS PASSWORD! It won't be displayed anywhere else");
        } else {
            log.info("Password was set from env.");
        }
        log.info("==================================================================");
    }

    private String generateSecurePassword() {
        byte[] randomBytes = new byte[12];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
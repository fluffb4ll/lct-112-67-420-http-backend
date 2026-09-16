package com.fluffb4ll.lct112HttpBackend.service;

import com.fluffb4ll.lct112HttpBackend.config.AuthProperties;
import com.fluffb4ll.lct112HttpBackend.entity.AuthTokenEntity;
import com.fluffb4ll.lct112HttpBackend.entity.UserEntity;
import com.fluffb4ll.lct112HttpBackend.repository.AuthRepository;
import com.fluffb4ll.lct112HttpBackend.repository.UserRepository;
import com.fluffb4ll.lct112HttpBackend.util.IdGeneratorUtil;
import com.fluffb4ll.lct112HttpBackend.util.RegexValidator;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final PasswordEncoder passEncoder;
    private final AuthProperties properties;

    public AuthService(UserRepository userRepository,
                       AuthRepository authRepository,
                       PasswordEncoder passEncoder,
                       AuthProperties properties) {
        this.userRepository = userRepository;
        this.authRepository = authRepository;
        this.passEncoder = passEncoder;
        this.properties = properties;
    }

    @Transactional
    public List<UUID> login(String nickname, String rawPassword) throws SecurityException {
        UserEntity user = userRepository.findByUsername(nickname)
                .orElseThrow(() -> new SecurityException("Wrong credentials"));
        if (!passEncoder.matches(rawPassword, user.getPasswordHash()))
            throw new SecurityException("Wrong credentials");

        UUID token = IdGeneratorUtil.generateId();
        OffsetDateTime expiresAt = OffsetDateTime.now()
                .plusHours(properties.tokenExpirationHrs())
                .plusMinutes(properties.tokenExpirationMins());
        AuthTokenEntity tokenEntity = new AuthTokenEntity(user.getId(), token, expiresAt);
        authRepository.save(tokenEntity);
        return List.of(user.getId(), token);
    }

//    @Transactional
//    public UUID signup(String nickname, String rawPassword) {
//        if (!RegexValidator.isValidPassword(rawPassword))
//            throw new SecurityException(
//                    "Invalid password. Password must be at least 8 characters long and contain " +
//                            "digits and Latin letters");
//        if (!RegexValidator.isValidNickname(nickname))
//            throw new SecurityException("Invalid nickname");
//        if (authRepository.findByNickname(nickname).isPresent())
//            throw new SecurityException("User already exists");
//
//        String encodedPassword = passEncoder.encode(rawPassword);
//        UserAuthEntity user = new UserAuthEntity(encodedPassword, nickname);
//        authRepository.save(user);
//        userRepository.save(new UserEntity(user.getId(), nickname));
//        return user.getId();
//    }

    @Transactional
    public boolean verifyAuthToken(UUID userId, UUID receivedAT) {
        AuthTokenEntity storedAT = authRepository.findTokenByUserId(userId).orElse(null);
        if (storedAT == null)
            return false;
        return storedAT.getToken().equals(receivedAT);
    }
}


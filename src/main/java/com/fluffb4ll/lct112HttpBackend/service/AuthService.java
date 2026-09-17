package com.fluffb4ll.lct112HttpBackend.service;

import com.fluffb4ll.lct112HttpBackend.config.AuthProperties;
import com.fluffb4ll.lct112HttpBackend.dto.response.LoginResponseDto;
import com.fluffb4ll.lct112HttpBackend.entity.AuthTokenEntity;
import com.fluffb4ll.lct112HttpBackend.entity.UserEntity;
import com.fluffb4ll.lct112HttpBackend.repository.AuthRepository;
import com.fluffb4ll.lct112HttpBackend.repository.UserRepository;
import com.fluffb4ll.lct112HttpBackend.util.IdGeneratorUtil;
import com.fluffb4ll.lct112HttpBackend.util.RegexValidator;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.naming.AuthenticationException;
import java.time.OffsetDateTime;
import java.util.*;

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
    public LoginResponseDto login(String nickname, String rawPassword) throws AuthenticationException {
        UserEntity user = userRepository.findByUsername(nickname)
                .orElseThrow(() -> new AuthenticationException("Wrong credentials"));
        if (!passEncoder.matches(rawPassword, user.getPasswordHash()))
            throw new AuthenticationException("Wrong credentials");

        UUID token = IdGeneratorUtil.generateId();
        OffsetDateTime expiresAt = OffsetDateTime.now()
                .plusHours(properties.tokenExpirationHrs())
                .plusMinutes(properties.tokenExpirationMins());
        AuthTokenEntity tokenEntity = new AuthTokenEntity(user.getId(), token, expiresAt);
        authRepository.save(tokenEntity);
        return createLoginResponse(user, tokenEntity);
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
        return authRepository.findTokenByUserId(userId)
                .filter(stored -> stored.getToken().equals(receivedAT))
                .filter(stored -> stored.getExpiresAt().isAfter(OffsetDateTime.now()))
                .isPresent();
    }

    private LoginResponseDto createLoginResponse(UserEntity user, AuthTokenEntity token) {
        // TODO: научить парсить дтошки, роли и права
        String role = null;
        Set<String> permissions = new HashSet<>();
        LoginResponseDto.DepartmentDto department = null;
        List<LoginResponseDto.StudyGroupDto> studyGroupDtos = new ArrayList<>();
        return new LoginResponseDto(
                null,
                token.getToken(),
                token.getExpiresAt(),
                new LoginResponseDto.UserInfoDto(
                        user.getId(),
                        user.getUsername(),
                        user.getFullName(),
                        role,
                        permissions,
                        department,
                        studyGroupDtos
                )
        );
    }
}


package com.fluffb4ll.lct112HttpBackend.service;

import com.fluffb4ll.lct112HttpBackend.config.AuthProperties;
import com.fluffb4ll.lct112HttpBackend.dto.response.LoginResponseDto;
import com.fluffb4ll.lct112HttpBackend.entity.*;
import com.fluffb4ll.lct112HttpBackend.repository.*;
import com.fluffb4ll.lct112HttpBackend.util.IdGeneratorUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.time.OffsetDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final PasswordEncoder passEncoder;
    private final AuthProperties properties;

    // TODO: разобраться с хэндлером
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

    @Transactional
    public void logout(UUID token) {
        authRepository.removeAuthTokenEntityById(token);
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
        LoginResponseDto.DepartmentDto departmentDto = null;
        if (user.getDepartment() != null)
            departmentDto = new LoginResponseDto.DepartmentDto(
                    user.getDepartment().getId(),
                    user.getDepartment().getCode(),
                    user.getDepartment().getName()
            );

        List<LoginResponseDto.StudyGroupDto> studyGroupDtos = user.getStudyGroups().stream()
                .map(g -> new LoginResponseDto.StudyGroupDto(
                        g.getId(),
                        g.getName(),
                        g.getTeacherId()
                ))
                .toList();

        return new LoginResponseDto(
                token.getToken(),
                token.getExpiresAt(),
                new LoginResponseDto.UserInfoDto(
                        user.getId(),
                        user.getUsername(),
                        user.getFullName(),
                        user.getRole().getName(),
                        user.getRole().getPermissionsAsSet(),
                        departmentDto,
                        studyGroupDtos
                )
        );
    }
}


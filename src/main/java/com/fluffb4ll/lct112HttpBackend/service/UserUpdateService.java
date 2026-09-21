package com.fluffb4ll.lct112HttpBackend.service;

import com.fluffb4ll.lct112HttpBackend.dto.request.CreateUserRequestDto;
import com.fluffb4ll.lct112HttpBackend.dto.request.DeleteUserRequestDto;
import com.fluffb4ll.lct112HttpBackend.dto.request.UpdateUserRequestDto;
import com.fluffb4ll.lct112HttpBackend.engine.factory.UserFactory;
import com.fluffb4ll.lct112HttpBackend.entity.DepartmentEntity;
import com.fluffb4ll.lct112HttpBackend.entity.RoleEntity;
import com.fluffb4ll.lct112HttpBackend.entity.UserEntity;
import com.fluffb4ll.lct112HttpBackend.model.enums.EntityType;
import com.fluffb4ll.lct112HttpBackend.model.enums.EventType;
import com.fluffb4ll.lct112HttpBackend.model.enums.Permissions;
import com.fluffb4ll.lct112HttpBackend.model.exceptions.UserUpdateException;
import com.fluffb4ll.lct112HttpBackend.repository.DepartmentRepository;
import com.fluffb4ll.lct112HttpBackend.repository.RolesRepository;
import com.fluffb4ll.lct112HttpBackend.repository.UserRepository;
import com.fluffb4ll.lct112HttpBackend.util.HttpRequestUtil;
import com.fluffb4ll.lct112HttpBackend.util.RegexValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserUpdateService {
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final DepartmentRepository departmentRepository;
    private final UserFactory userFactory;
    private final AuditService auditService;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createUser(UUID token, CreateUserRequestDto request) throws AuthenticationException {
        UserEntity user = verifyAuthToken(token, request.roleId());

        if (userRepository.existsByUsername(request.username()))
            throw new UserUpdateException("User already exists");

        try {
            RoleEntity role = rolesRepository.getReferenceById(request.roleId());

            DepartmentEntity department = null;
            if (request.departmentId() != null)
                    department = departmentRepository.getReferenceById(request.departmentId());

            if (RegexValidator.isNotAValidUsername(request.username()))
                throw new UserUpdateException("Bad username formatting");
            if (RegexValidator.isNotAValidPassword(request.password()))
                throw new UserUpdateException("Bad password formatting");
            if (RegexValidator.isNotAValidFullName(request.fullName()))
                throw new UserUpdateException("Bas full name formatting");

            UserEntity newUser = userFactory.createUserWithPasswordHashing(
                    request.username(),
                    request.password(),
                    request.fullName(),
                    role,
                    department
            );

            userRepository.save(newUser);
            auditService.logAction(
                    user.getId(),
                    EventType.USER_CREATION,
                    EntityType.USER,
                    newUser.getId(),
                    null,
                    newUser,
                    HttpRequestUtil.getClientIp()
            );
        } catch (DataIntegrityViolationException e) {
            throw new UserUpdateException("Invalid arguments");
        }
    }

    @Transactional
    public void deleteUser(UUID token, DeleteUserRequestDto request) throws AuthenticationException {
        UserEntity targetUser = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserUpdateException("User not found"));

        UserEntity user = verifyAuthToken(token, targetUser);

        if (user.getId().equals(targetUser.getId()))
            throw new UserUpdateException("Suicide is prohibited :)");

        userRepository.delete(targetUser);
    }

    @Transactional
    public void updateUser(UUID token, UpdateUserRequestDto request) throws AuthenticationException {
        UserEntity targetUser = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserUpdateException("User not found"));

        UserEntity currentUser = verifyAuthToken(token, targetUser);
        boolean isSelfUpdate = targetUser.getId().equals(currentUser.getId());

        if (request.username() != null && !request.username().equals(targetUser.getUsername())) {
            if (RegexValidator.isNotAValidUsername(request.username())) {
                throw new UserUpdateException("Bad username formatting");
            }
            if (userRepository.existsByUsername(request.username())) {
                throw new UserUpdateException("Username already taken");
            }
            targetUser.setUsername(request.username());
        }

        if (request.password() != null) {
            if (RegexValidator.isNotAValidPassword(request.password())) {
                throw new UserUpdateException("Bad password formatting");
            }
            targetUser.setPasswordHash(passwordEncoder.encode(request.password()));
        }

        if (request.fullName() != null) {
            if (RegexValidator.isNotAValidFullName(request.fullName()))
                throw new UserUpdateException("Bad full name formatting");
            targetUser.setFullName(request.fullName().trim());
        }

        if (request.roleId() != null) {
            RoleEntity role = rolesRepository.findById(request.roleId())
                    .orElseThrow(() -> new UserUpdateException("Invalid role id"));
            targetUser.setRole(role);
        }

        if (request.removeDepartment()) {
            targetUser.setDepartment(null);
        } else if (request.departmentId() != null) {
            DepartmentEntity department = departmentRepository.findById(request.departmentId())
                    .orElseThrow(() -> new UserUpdateException("Invalid department id"));
            targetUser.setDepartment(department);
        }

        if (request.isActive() != null) {
            if (!request.isActive() && isSelfUpdate) {
                throw new UserUpdateException("Suicide is prohibited :)");
            }
            targetUser.setActive(request.isActive());
        }

        if (request.mustChangePassword() != null) {
            if (request.mustChangePassword() && isSelfUpdate) {
                throw new UserUpdateException("It's your password - simply change it!");
            }
            targetUser.setMustChangePassword(request.mustChangePassword());
        }
    }

    private UserEntity verifyAuthToken(UUID token, UserEntity targetUser) throws AuthenticationException {
        return authService.verifyAuthToken(token,
                targetUser.getRole().getId() == 0 ?
                        Permissions.ADMIN_CAN_EDIT_ADMINS :
                        Permissions.ADMIN_CAN_EDIT_USERS);
    }

    private UserEntity verifyAuthToken(UUID token, int roleId) throws AuthenticationException {
        return authService.verifyAuthToken(token,
                roleId == 0 ?
                        Permissions.ADMIN_CAN_EDIT_ADMINS :
                        Permissions.ADMIN_CAN_EDIT_USERS);
    }
}

package com.fluffb4ll.lct112HttpBackend.service;

import com.fluffb4ll.lct112HttpBackend.dto.request.CreateUserRequestDto;
import com.fluffb4ll.lct112HttpBackend.engine.factory.UserFactory;
import com.fluffb4ll.lct112HttpBackend.entity.DepartmentEntity;
import com.fluffb4ll.lct112HttpBackend.entity.RoleEntity;
import com.fluffb4ll.lct112HttpBackend.entity.UserEntity;
import com.fluffb4ll.lct112HttpBackend.model.enums.EntityType;
import com.fluffb4ll.lct112HttpBackend.model.enums.EventType;
import com.fluffb4ll.lct112HttpBackend.model.enums.Permissions;
import com.fluffb4ll.lct112HttpBackend.model.exceptions.UserCreationException;
import com.fluffb4ll.lct112HttpBackend.repository.DepartmentRepository;
import com.fluffb4ll.lct112HttpBackend.repository.RolesRepository;
import com.fluffb4ll.lct112HttpBackend.repository.UserRepository;
import com.fluffb4ll.lct112HttpBackend.util.HttpRequestUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserCreationService {
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final DepartmentRepository departmentRepository;
    private final UserFactory userFactory;
    private final AuditService auditService;
    private final AuthService authService;

    @Transactional
    public void createUser(UUID token, CreateUserRequestDto request) throws AuthenticationException {
        UserEntity user = authService.verifyAuthToken(
                token,
                request.roleId() == 0 ?
                        Permissions.ADMIN_CAN_EDIT_ADMINS :
                        Permissions.ADMIN_CAN_EDIT_USERS
        );

        if (userRepository.existsByUsername(request.username()))
            throw new UserCreationException("User already exists");

        try {
            RoleEntity role = rolesRepository.getReferenceById(request.roleId());

            DepartmentEntity department = null;
            if (request.departmentId() != null)
                    department = departmentRepository.getReferenceById(request.departmentId());

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
            throw new UserCreationException("Invalid arguments");
        }
    }
}

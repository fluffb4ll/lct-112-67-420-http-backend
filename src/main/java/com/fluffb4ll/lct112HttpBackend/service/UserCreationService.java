package com.fluffb4ll.lct112HttpBackend.service;

import com.fluffb4ll.lct112HttpBackend.dto.request.CreateUserRequestDto;
import com.fluffb4ll.lct112HttpBackend.engine.factory.UserFactory;
import com.fluffb4ll.lct112HttpBackend.entity.DepartmentEntity;
import com.fluffb4ll.lct112HttpBackend.entity.RoleEntity;
import com.fluffb4ll.lct112HttpBackend.entity.UserEntity;
import com.fluffb4ll.lct112HttpBackend.model.exceptions.UserCreationException;
import com.fluffb4ll.lct112HttpBackend.repository.DepartmentRepository;
import com.fluffb4ll.lct112HttpBackend.repository.RolesRepository;
import com.fluffb4ll.lct112HttpBackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCreationService {
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final DepartmentRepository departmentRepository;
    private final UserFactory userFactory;

    @Transactional
    public void createUser(CreateUserRequestDto request) {
        if (userRepository.existsByUsername(request.username()))
            throw new UserCreationException("User already exists");

        try {
            RoleEntity role = rolesRepository.getReferenceById(request.roleId());

            DepartmentEntity department = null;
            if (request.departmentId() != null)
                    department = departmentRepository.getReferenceById(request.departmentId());

            UserEntity user = userFactory.createUserWithPasswordHashing(
                    request.username(),
                    request.password(),
                    request.fullName(),
                    role,
                    department
            );

            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserCreationException("Invalid arguments");
        }
    }
}

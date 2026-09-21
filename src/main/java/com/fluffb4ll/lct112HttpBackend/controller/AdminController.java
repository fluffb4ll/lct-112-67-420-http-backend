package com.fluffb4ll.lct112HttpBackend.controller;

import com.fluffb4ll.lct112HttpBackend.dto.request.CreateUserRequestDto;
import com.fluffb4ll.lct112HttpBackend.model.enums.Permissions;
import com.fluffb4ll.lct112HttpBackend.service.AuthService;
import com.fluffb4ll.lct112HttpBackend.service.UserCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserCreationService userCreationService;
    private final AuthService authService;

    @PostMapping("/createUser")
    public ResponseEntity<Void> createUser(
            @CookieValue(name = "AUTH_TOKEN") String token,
            @RequestBody CreateUserRequestDto request
    ) throws AuthenticationException {
        authService.verifyAuthToken(
                UUID.fromString(token),
                request.roleId() == 0 ?
                        Permissions.ADMIN_CAN_EDIT_ADMINS :
                        Permissions.ADMIN_CAN_EDIT_USERS
        );
        userCreationService.createUser(request);
        return ResponseEntity.ok().body(null);
    }
}

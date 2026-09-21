package com.fluffb4ll.lct112HttpBackend.controller;

import com.fluffb4ll.lct112HttpBackend.dto.request.CreateUserRequestDto;
import com.fluffb4ll.lct112HttpBackend.dto.request.DeleteUserRequestDto;
import com.fluffb4ll.lct112HttpBackend.service.UserUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserUpdateService userUpdateService;

    @PostMapping("/createUser")
    public ResponseEntity<Void> createUser(
            @CookieValue(name = "AUTH_TOKEN") String token,
            @RequestBody CreateUserRequestDto request
    ) throws AuthenticationException {
        userUpdateService.createUser(UUID.fromString(token), request);
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/deleteUser")
    public ResponseEntity<Void> deleteUser(
            @CookieValue(name = "AUTH_TOKEN") String token,
            @RequestBody DeleteUserRequestDto request
    ) throws AuthenticationException {
        userUpdateService.deleteUser(UUID.fromString(token), request);
        return ResponseEntity.ok().body(null);
    }
}

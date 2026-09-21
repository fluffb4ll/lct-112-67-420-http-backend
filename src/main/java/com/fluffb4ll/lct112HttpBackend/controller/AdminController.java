package com.fluffb4ll.lct112HttpBackend.controller;

import com.fluffb4ll.lct112HttpBackend.dto.request.CreateUserRequestDto;
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

    @PostMapping("/createUser")
    public ResponseEntity<Void> createUser(
            @CookieValue(name = "AUTH_TOKEN") String token,
            @RequestBody CreateUserRequestDto request
    ) throws AuthenticationException {
        userCreationService.createUser(UUID.fromString(token), request);
        return ResponseEntity.ok().body(null);
    }
}

package com.fluffb4ll.lct112HttpBackend.controller;

import com.fluffb4ll.lct112HttpBackend.dto.request.CreateUserRequestDto;
import com.fluffb4ll.lct112HttpBackend.dto.request.DeleteUserRequestDto;
import com.fluffb4ll.lct112HttpBackend.dto.request.UpdateUserRequestDto;
import com.fluffb4ll.lct112HttpBackend.dto.response.CreateUserResponseDto;
import com.fluffb4ll.lct112HttpBackend.dto.response.UserInfoDto;
import com.fluffb4ll.lct112HttpBackend.service.UserUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.naming.AuthenticationException;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserUpdateService userUpdateService;

    @PostMapping("/users/create")
    public ResponseEntity<CreateUserResponseDto> createUser(
            @CookieValue(name = "AUTH_TOKEN") String token,
            @RequestBody CreateUserRequestDto request
    ) throws AuthenticationException {
        UUID userId = userUpdateService.createUser(UUID.fromString(token), request);
        URI location = URI.create("/api/admin/users/" + userId);
        return ResponseEntity.created(location).body(new CreateUserResponseDto(userId));
    }

    @DeleteMapping("/users/{uuid}")
    public ResponseEntity<Void> deleteUser(
            @CookieValue(name = "AUTH_TOKEN") String token,
            @PathVariable("uuid") UUID userId
    ) throws AuthenticationException {
        userUpdateService.deleteUser(UUID.fromString(token), userId);
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/users/update")
    public ResponseEntity<Void> updateUser(
            @CookieValue(name = "AUTH_TOKEN") String token,
            @RequestBody UpdateUserRequestDto request
    ) throws AuthenticationException {
        userUpdateService.updateUser(UUID.fromString(token), request);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("users/{uuid}")
    public ResponseEntity<UserInfoDto> getUser(
            @CookieValue(name = "AUTH_TOKEN") String token,
            @PathVariable("uuid") UUID userId
    ) throws AuthenticationException {
        return ResponseEntity.ok().body(userUpdateService.getUser(UUID.fromString(token), userId));
    }
}

package com.fluffb4ll.lct112HttpBackend.controller;

import com.fluffb4ll.lct112HttpBackend.dto.request.CreateUserRequestDto;
import com.fluffb4ll.lct112HttpBackend.dto.request.DeleteUserRequestDto;
import com.fluffb4ll.lct112HttpBackend.dto.request.UpdateUserRequestDto;
import com.fluffb4ll.lct112HttpBackend.dto.response.UserInfoDto;
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

    // TODO: редирект на созданный объект?
    @PostMapping("/user/create")
    public ResponseEntity<Void> createUser(
            @CookieValue(name = "AUTH_TOKEN") String token,
            @RequestBody CreateUserRequestDto request
    ) throws AuthenticationException {
        userUpdateService.createUser(UUID.fromString(token), request);
        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping("/user/{uuid}")
    public ResponseEntity<Void> deleteUser(
            @CookieValue(name = "AUTH_TOKEN") String token,
            @PathVariable("uuid") UUID userId
    ) throws AuthenticationException {
        userUpdateService.deleteUser(UUID.fromString(token), userId);
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/user/update")
    public ResponseEntity<Void> updateUser(
            @CookieValue(name = "AUTH_TOKEN") String token,
            @RequestBody UpdateUserRequestDto request
    ) throws AuthenticationException {
        userUpdateService.updateUser(UUID.fromString(token), request);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("user/{uuid}")
    public ResponseEntity<UserInfoDto> getUser(
            @CookieValue(name = "AUTH_TOKEN") String token,
            @PathVariable("uuid") UUID userId
    ) throws AuthenticationException {
        return ResponseEntity.ok().body(userUpdateService.getUser(UUID.fromString(token), userId));
    }
}

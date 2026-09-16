package com.fluffb4ll.lct112HttpBackend.controller;

import com.fluffb4ll.lct112HttpBackend.dto.request.AuthRequestDto;
import com.fluffb4ll.lct112HttpBackend.dto.response.AuthResponseDto;
import com.fluffb4ll.lct112HttpBackend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto authDto) {
        try {
            List<UUID> result = authService.login(authDto.username(), authDto.password());
            return ResponseEntity.ok(AuthResponseDto.loginOk(result));
        } catch (SecurityException e) {
            return ResponseEntity.badRequest().body(AuthResponseDto.error(e.getMessage()));
        }
    }

//    @PostMapping("/signup")
//    public ResponseEntity<AuthResponseDto> signup(@RequestBody AuthRequestDto authDto) {
//        try {
//            UUID token = authService.signup(authDto.username(), authDto.password());
//            return ResponseEntity.ok(AuthResponseDto.signupOk(token));
//        } catch (SecurityException e) {
//            return ResponseEntity.badRequest().body(AuthResponseDto.error(e.getMessage()));
//        }
//    }
}


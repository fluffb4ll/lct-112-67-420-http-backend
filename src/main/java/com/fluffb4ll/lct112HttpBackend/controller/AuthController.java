package com.fluffb4ll.lct112HttpBackend.controller;

import com.fluffb4ll.lct112HttpBackend.dto.request.LoginRequestDto;
import com.fluffb4ll.lct112HttpBackend.dto.response.LoginResponseDto;
import com.fluffb4ll.lct112HttpBackend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto authDto) {
        return ResponseEntity.ok(authService.login(authDto.username(), authDto.password()));
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


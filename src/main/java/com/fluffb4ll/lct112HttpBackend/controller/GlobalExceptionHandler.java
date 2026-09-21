package com.fluffb4ll.lct112HttpBackend.controller;

import com.fluffb4ll.lct112HttpBackend.dto.response.ErrorResponseDto;
import javax.naming.AuthenticationException;

import com.fluffb4ll.lct112HttpBackend.model.exceptions.AuthTokenExpiredException;
import com.fluffb4ll.lct112HttpBackend.model.exceptions.UserCreationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDto> processSecurityError(AuthenticationException e) {
        ErrorResponseDto response = new ErrorResponseDto(e.getMessage());
        return ResponseEntity.status(401).body(response);
    }

    @ExceptionHandler(UserCreationException.class)
    public ResponseEntity<ErrorResponseDto> processUserCreationError(UserCreationException e) {
        ErrorResponseDto response = new ErrorResponseDto(e.getMessage());
        return ResponseEntity.status(409).body(response);
    }

    @ExceptionHandler(AuthTokenExpiredException.class)
    public ResponseEntity<ErrorResponseDto> processAuthTokenExpiredError(AuthTokenExpiredException e) {
        ErrorResponseDto response = new ErrorResponseDto(e.getMessage());
        ResponseCookie deleteCookie = ResponseCookie.from("AUTH_TOKEN", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        return ResponseEntity.status(401)
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body(response);
    }
}

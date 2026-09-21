package com.fluffb4ll.lct112HttpBackend.controller;

import com.fluffb4ll.lct112HttpBackend.dto.response.ErrorResponseDto;
import javax.naming.AuthenticationException;
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
}

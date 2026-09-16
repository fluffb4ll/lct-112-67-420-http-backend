package com.fluffb4ll.lct112HttpBackend.controller;

import com.fluffb4ll.lct112HttpBackend.dto.response.errors.SecurityErrorResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // TODO: исправить проверку типа ошибки, возможно - сделать кастомное исключение
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<SecurityErrorResponseDto> processSecurityError(SecurityException e) {
        SecurityErrorResponseDto response = new SecurityErrorResponseDto(e.getMessage());
        if (e.getMessage().equals("Wrong credentials"))
            return ResponseEntity.status(401).body(response);
        else
            return ResponseEntity.status(500).body(response);
    }

}

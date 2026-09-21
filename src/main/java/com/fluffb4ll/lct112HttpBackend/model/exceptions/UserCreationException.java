package com.fluffb4ll.lct112HttpBackend.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UserCreationException extends RuntimeException {
    public UserCreationException(String message) {
        super(message);
    }
}

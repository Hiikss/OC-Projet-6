package com.openclassrooms.mddapi.application.authentication;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthException extends RuntimeException {

    private final HttpStatus status;

    public AuthException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}

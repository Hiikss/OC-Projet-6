package com.openclassrooms.mddapi.application.authentication;

public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}

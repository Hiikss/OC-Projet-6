package com.openclassrooms.mddapi.application.authentication;

public record LoginDto(
        String login,
        String password
) {
}

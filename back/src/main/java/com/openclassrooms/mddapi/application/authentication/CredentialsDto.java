package com.openclassrooms.mddapi.application.authentication;

public record CredentialsDto(
        String login,
        String password
) {
}

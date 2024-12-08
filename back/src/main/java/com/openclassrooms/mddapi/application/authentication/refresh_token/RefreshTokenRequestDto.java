package com.openclassrooms.mddapi.application.authentication.refresh_token;

public record RefreshTokenRequestDto(String refreshToken, String userId) {
}

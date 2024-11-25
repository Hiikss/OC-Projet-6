package com.openclassrooms.mddapi.application.authentication.refresh_token;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(String email);

    void validateRefreshToken(RefreshTokenRequestDto refreshTokenRequestDto);
}

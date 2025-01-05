package com.openclassrooms.mddapi.application.authentication;

import com.openclassrooms.mddapi.application.authentication.refresh_token.RefreshTokenRequestDto;
import com.openclassrooms.mddapi.application.authentication.refresh_token.RefreshTokenService;
import com.openclassrooms.mddapi.application.security.UserAuthenticationProvider;
import com.openclassrooms.mddapi.domains.user.UserRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Validated
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserAuthenticationProvider userAuthenticationProvider;
    private final RefreshTokenService refreshTokenService;

    /**
     * Log the user in. Return 401 if bad credentials
     * @param loginDto
     * @return the user's information, access token and refresh token
     */
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticatedUserDto login(@RequestBody LoginDto loginDto) {
        log.info("[Auth Controller] Attempting to login");

        AuthenticatedUserDto user = authService.login(loginDto);
        user.setAccessToken(userAuthenticationProvider.createAccessToken(user.getUserId()));
        user.setRefreshToken(refreshTokenService.createRefreshToken(user.getUserId()).getToken());

        return user;
    }

    /**
     * Create a new user. Return 409 if email or username is already used
     * @param userRequestDto
     * @return the user's information, access token and refresh token
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthenticatedUserDto register(@Valid @RequestBody UserRequestDto userRequestDto) {
        log.info("[Auth Controller] Attempting to register");

        AuthenticatedUserDto user = authService.register(userRequestDto);
        user.setAccessToken(userAuthenticationProvider.createAccessToken(user.getUserId()));
        user.setRefreshToken(refreshTokenService.createRefreshToken(user.getUserId()).getToken());

        return user;
    }

    /**
     * Refresh the user's access token with the refresh token. If refresh token not valid, return 401
     * @param refreshTokenRequestDto
     * @return the user's information, new access token and new refresh token
     */
    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticatedUserDto refresh(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        log.info("[Auth Controller] Attempting to refresh");

        refreshTokenService.validateRefreshToken(refreshTokenRequestDto);

        AuthenticatedUserDto user = authService.getAuthenticatedUser(refreshTokenRequestDto.userId());
        user.setAccessToken(userAuthenticationProvider.createAccessToken(user.getUserId()));
        user.setRefreshToken(refreshTokenService.createRefreshToken(user.getUserId()).getToken());

        return user;
    }
}

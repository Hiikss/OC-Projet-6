package com.openclassrooms.mddapi.application.authentication;

public interface AuthService {

    AuthenticatedUserDto login(LoginDto loginDto);

    AuthenticatedUserDto register(RegisterDto registerDto);

    AuthenticatedUserDto getAuthenticatedUser(String email);
}

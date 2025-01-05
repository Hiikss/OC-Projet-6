package com.openclassrooms.mddapi.application.authentication;

import com.openclassrooms.mddapi.domains.user.UserRequestDto;

public interface AuthService {

    AuthenticatedUserDto login(LoginDto loginDto);

    AuthenticatedUserDto register(UserRequestDto UserRequestDto);

    AuthenticatedUserDto getAuthenticatedUser(String userId);
}

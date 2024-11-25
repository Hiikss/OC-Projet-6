package com.openclassrooms.mddapi.application.authentication;

import com.openclassrooms.mddapi.domains.user.UserRequestDto;

public interface AuthService {

    AuthenticatedUserDto login(CredentialsDto credentialsDto);

    AuthenticatedUserDto register(UserRequestDto userRequestDto);

    AuthenticatedUserDto getAuthenticatedUser(String email);
}

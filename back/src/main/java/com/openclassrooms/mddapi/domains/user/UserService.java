package com.openclassrooms.mddapi.domains.user;

public interface UserService {

    UserResponseDto getUserById(String userId);

    UserResponseDto updateUser(String userId, UserRequestDto userRequestDto, String authenticatedUserId);
}

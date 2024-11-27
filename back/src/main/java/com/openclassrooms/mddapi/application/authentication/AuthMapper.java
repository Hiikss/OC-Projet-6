package com.openclassrooms.mddapi.application.authentication;

import com.openclassrooms.mddapi.domains.user.User;
import com.openclassrooms.mddapi.domains.user.UserRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(target = "accessToken", ignore = true)
    @Mapping(target = "refreshToken", ignore = true)
    AuthenticatedUserDto toAuthenticatedUserDto(User user);

    User toUser(UserRequestDto userRequestDto);
}

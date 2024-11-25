package com.openclassrooms.mddapi.application.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticatedUserDto {

    private String email;

    private String username;

    private String accessToken;

    private String refreshToken;

}

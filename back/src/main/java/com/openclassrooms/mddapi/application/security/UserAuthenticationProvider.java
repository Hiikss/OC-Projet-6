package com.openclassrooms.mddapi.application.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.openclassrooms.mddapi.application.authentication.AuthService;
import com.openclassrooms.mddapi.application.authentication.AuthenticatedUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class UserAuthenticationProvider {

    private final AuthService authService;

    private final SecurityProperties securityProperties;

    public String createAccessToken(AuthenticatedUserDto user) {
        Date now = new Date();
        Date validity = Date.from(Instant.now().plus(securityProperties.getAccessTokenDuration()));

        return JWT.create()
                .withSubject(user.getEmail())
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .sign(Algorithm.HMAC256(securityProperties.getSecretKey()));
    }

    public Authentication validateAccessToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(securityProperties.getSecretKey());

        JWTVerifier verifier = JWT.require(algorithm)
                .build();

        DecodedJWT decoded = verifier.verify(token);

        AuthenticatedUserDto user = authService.getAuthenticatedUser(decoded.getSubject());

        return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
    }

}

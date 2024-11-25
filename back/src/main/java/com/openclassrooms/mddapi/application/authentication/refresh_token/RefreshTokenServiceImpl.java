package com.openclassrooms.mddapi.application.authentication.refresh_token;

import com.openclassrooms.mddapi.application.authentication.AuthException;
import com.openclassrooms.mddapi.application.security.SecurityProperties;
import com.openclassrooms.mddapi.domains.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    private final SecurityProperties securityProperties;

    @Override
    public RefreshToken createRefreshToken(String email) {
        Optional<RefreshToken> oRefreshToken = refreshTokenRepository.findByUserEmail(email);

        oRefreshToken.ifPresent(refreshTokenRepository::delete);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(userRepository.findByEmail(email).orElseThrow(() -> new AuthException("User not found")))
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plus(securityProperties.getRefreshTokenDuration()))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public void validateRefreshToken(RefreshTokenRequestDto refreshTokenRequestDto) {
        RefreshToken refreshToken = refreshTokenRepository.findByTokenAndUserEmail(refreshTokenRequestDto.refreshToken(), refreshTokenRequestDto.email())
                .orElseThrow(() -> new AuthException("Refresh token not found"));

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new AuthException("Refresh token is expired");
        }

        if (!refreshToken.getUser().getEmail().equals(refreshTokenRequestDto.email())) {
            throw new AuthException("Invalid refresh token");
        }
    }

}

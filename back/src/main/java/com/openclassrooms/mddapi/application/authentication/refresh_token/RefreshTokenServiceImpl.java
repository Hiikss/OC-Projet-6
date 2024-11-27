package com.openclassrooms.mddapi.application.authentication.refresh_token;

import com.openclassrooms.mddapi.application.authentication.AuthException;
import com.openclassrooms.mddapi.application.security.SecurityProperties;
import com.openclassrooms.mddapi.domains.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public RefreshToken createRefreshToken(String userId) {
        Optional<RefreshToken> oRefreshToken = refreshTokenRepository.findById(userId);

        oRefreshToken.ifPresent(refreshTokenRepository::delete);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(userRepository.findById(userId).orElseThrow(() -> new AuthException("User not found", HttpStatus.NOT_FOUND)))
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plus(securityProperties.getRefreshTokenDuration()))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public void validateRefreshToken(RefreshTokenRequestDto refreshTokenRequestDto) {
        RefreshToken refreshToken = refreshTokenRepository.findByTokenAndId(refreshTokenRequestDto.refreshToken(), refreshTokenRequestDto.id())
                .orElseThrow(() -> new AuthException("Refresh token not found", HttpStatus.NOT_FOUND));

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new AuthException("Refresh token is expired", HttpStatus.UNAUTHORIZED);
        }

        if (!refreshToken.getUser().getId().equals(refreshTokenRequestDto.id())) {
            throw new AuthException("Invalid refresh token", HttpStatus.UNAUTHORIZED);
        }
    }

}

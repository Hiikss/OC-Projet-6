package com.openclassrooms.mddapi.application.authentication.refresh_token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    Optional<RefreshToken> findByTokenAndUserId(String token, String userId);

    Optional<RefreshToken> findByUserId(String userId);
}

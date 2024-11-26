package com.openclassrooms.mddapi.application.authentication;

import com.openclassrooms.mddapi.domains.user.User;
import com.openclassrooms.mddapi.domains.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final AuthMapper authMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthenticatedUserDto login(LoginDto loginDto) {
        Optional<User> oUser = userRepository.findByLogin(loginDto.login());

        if (oUser.isPresent() && passwordEncoder.matches(loginDto.password(), oUser.get().getPassword())) {
            return authMapper.toAuthenticatedUserDto(oUser.get());
        }

        throw new AuthException("Bad credentials", HttpStatus.BAD_REQUEST);
    }

    @Override
    public AuthenticatedUserDto register(RegisterDto registerDto) {
        Optional<User> oUser = userRepository.findByEmail(registerDto.email());
        if (oUser.isPresent()) {
            throw new AuthException("Email is already used", HttpStatus.BAD_REQUEST);
        }

        oUser = userRepository.findByUsername(registerDto.username());
        if (oUser.isPresent()) {
            throw new AuthException("Username is already used", HttpStatus.BAD_REQUEST);
        }

        User user = authMapper.toUser(registerDto);
        user.setPassword(passwordEncoder.encode(registerDto.password()));

        User savedUser = userRepository.save(user);

        return authMapper.toAuthenticatedUserDto(savedUser);
    }

    @Override
    public AuthenticatedUserDto getAuthenticatedUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException("User not found", HttpStatus.NOT_FOUND));

        return authMapper.toAuthenticatedUserDto(user);
    }
}

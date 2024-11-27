package com.openclassrooms.mddapi.application.authentication;

import com.openclassrooms.mddapi.domains.user.User;
import com.openclassrooms.mddapi.domains.user.UserRepository;
import com.openclassrooms.mddapi.domains.user.UserRequestDto;
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

        throw new AuthException("Bad credentials", HttpStatus.UNAUTHORIZED);
    }

    @Override
    public AuthenticatedUserDto register(UserRequestDto userRequestDto) {
        Optional<User> oUser = userRepository.findByEmail(userRequestDto.email());
        if (oUser.isPresent()) {
            throw new AuthException("Email is already used", HttpStatus.CONFLICT);
        }

        oUser = userRepository.findByUsername(userRequestDto.username());
        if (oUser.isPresent()) {
            throw new AuthException("Username is already used", HttpStatus.CONFLICT);
        }

        User user = authMapper.toUser(userRequestDto);
        user.setPassword(passwordEncoder.encode(userRequestDto.password()));

        User savedUser = userRepository.save(user);

        return authMapper.toAuthenticatedUserDto(savedUser);
    }

    @Override
    public AuthenticatedUserDto getAuthenticatedUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException("User not found", HttpStatus.NOT_FOUND));

        return authMapper.toAuthenticatedUserDto(user);
    }
}

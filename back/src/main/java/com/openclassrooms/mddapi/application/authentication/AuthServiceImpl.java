package com.openclassrooms.mddapi.application.authentication;

import com.openclassrooms.mddapi.domains.user.*;
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
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Log the user by its email or username
     * @param loginDto
     * @return the authenticated user
     */
    @Override
    public AuthenticatedUserDto login(LoginDto loginDto) {
        Optional<User> oUser = userRepository.findByLogin(loginDto.login());

        if (oUser.isPresent() && passwordEncoder.matches(loginDto.password(), oUser.get().getPassword())) {
            return authMapper.toAuthenticatedUserDto(oUser.get());
        }

        throw new AuthException("Bad credentials", HttpStatus.UNAUTHORIZED);
    }

    /**
     * Register a new user. Check if the email and the username is already used. Encode the password aswell
     * @param userRequestDto
     * @return the authenticated user
     */
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

        User user = userMapper.toUser(userRequestDto);
        user.setPassword(passwordEncoder.encode(userRequestDto.password()));

        User savedUser = userRepository.save(user);

        return authMapper.toAuthenticatedUserDto(savedUser);
    }

    /**
     * Get the authenticated user based on the user id
     * @param userId
     * @return the authenticated user
     */
    @Override
    public AuthenticatedUserDto getAuthenticatedUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User not found", HttpStatus.NOT_FOUND));

        return authMapper.toAuthenticatedUserDto(user);
    }
}

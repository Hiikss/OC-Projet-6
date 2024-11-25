package com.openclassrooms.mddapi.application.authentication;

import com.openclassrooms.mddapi.domains.user.User;
import com.openclassrooms.mddapi.domains.user.UserRepository;
import com.openclassrooms.mddapi.domains.user.UserRequestDto;
import lombok.RequiredArgsConstructor;
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
    public AuthenticatedUserDto login(CredentialsDto credentialsDto) {
        User user = userRepository.findByLogin(credentialsDto.login())
                .orElseThrow(() -> new AuthException("User not found"));

        if (passwordEncoder.matches(credentialsDto.password(), user.getPassword())) {
            return authMapper.toAuthenticatedUserDto(user);
        }

        throw new AuthException("Bad credentials");
    }

    @Override
    public AuthenticatedUserDto register(UserRequestDto userRequestDto) {
        Optional<User> oUser = userRepository.findByEmail(userRequestDto.email());
        if (oUser.isPresent()) {
            throw new AuthException("Email is already used");
        }

        oUser = userRepository.findByUsername(userRequestDto.username());
        if (oUser.isPresent()) {
            throw new AuthException("Username is already used");
        }

        User user = authMapper.toUser(userRequestDto);
        user.setPassword(passwordEncoder.encode(userRequestDto.password()));

        User savedUser = userRepository.save(user);

        return authMapper.toAuthenticatedUserDto(savedUser);
    }


    @Override
    public AuthenticatedUserDto getAuthenticatedUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException("User not found"));

        return authMapper.toAuthenticatedUserDto(user);
    }
}

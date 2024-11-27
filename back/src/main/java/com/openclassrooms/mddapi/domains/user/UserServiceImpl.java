package com.openclassrooms.mddapi.domains.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto getUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User not found", HttpStatus.NOT_FOUND));

        return userMapper.toUserDto(user);
    }

    @Override
    public UserResponseDto updateUser(String userId, UserRequestDto userRequestDto, String authenticatedUserId) {
        if (!userId.equals(authenticatedUserId)) {
            throw new UserException("Can't update this user", HttpStatus.FORBIDDEN);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User not found", HttpStatus.NOT_FOUND));

        Optional<User> oUser = userRepository.findByEmail(user.getEmail());
        if (oUser.isPresent() && !userId.equals(oUser.get().getId())) {
            throw new UserException("Email is already used", HttpStatus.CONFLICT);
        }

        oUser = userRepository.findByUsername(user.getUsername());
        if (oUser.isPresent() && !userId.equals(oUser.get().getId())) {
            throw new UserException("Username is already used", HttpStatus.CONFLICT);
        }

        user.setEmail(userRequestDto.email());
        user.setUsername(userRequestDto.username());
        user.setPassword(passwordEncoder.encode(userRequestDto.password()));

        User savedUser = userRepository.save(user);

        return userMapper.toUserDto(savedUser);
    }

}

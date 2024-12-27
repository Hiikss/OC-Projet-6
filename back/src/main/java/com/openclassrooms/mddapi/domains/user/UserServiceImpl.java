package com.openclassrooms.mddapi.domains.user;

import com.openclassrooms.mddapi.domains.topic.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final UserMapper userMapper;
    private final TopicMapper topicMapper;
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

        Optional<User> oUser = userRepository.findByEmail(userRequestDto.email());
        if (oUser.isPresent() && !userId.equals(oUser.get().getId())) {
            throw new UserException("Email is already used", HttpStatus.CONFLICT);
        }

        oUser = userRepository.findByUsername(userRequestDto.username());
        if (oUser.isPresent() && !userId.equals(oUser.get().getId())) {
            throw new UserException("Username is already used", HttpStatus.CONFLICT);
        }

        user.setEmail(userRequestDto.email());
        user.setUsername(userRequestDto.username());
        user.setPassword(passwordEncoder.encode(userRequestDto.password()));

        User savedUser = userRepository.save(user);

        return userMapper.toUserDto(savedUser);
    }

    @Override
    public UserResponseDto partialUpdateUser(String userId, Map<String, String> updatedFiels, String authenticatedUserId) {
        if (!userId.equals(authenticatedUserId)) {
            throw new UserException("Can't update this user", HttpStatus.FORBIDDEN);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User not found", HttpStatus.NOT_FOUND));


        updatedFiels.forEach((key, value) -> {
            switch (key) {
                case "email" -> {
                    Optional<User> oUser = userRepository.findByEmail(value);
                    if (oUser.isPresent() && !userId.equals(oUser.get().getId())) {
                        throw new UserException("Email is already used", HttpStatus.CONFLICT);
                    }
                    user.setEmail(value);
                }
                case "username" -> {
                    Optional<User> oUser = userRepository.findByUsername(user.getUsername());
                    if (oUser.isPresent() && !userId.equals(oUser.get().getId())) {
                        throw new UserException("Username is already used", HttpStatus.CONFLICT);
                    }
                    user.setUsername(value);
                }
                case "password" -> user.setPassword(passwordEncoder.encode(value));

                default -> throw new UserException("Invalid key", HttpStatus.BAD_REQUEST);
            }
        });

        User savedUser = userRepository.save(user);

        return userMapper.toUserDto(savedUser);
    }

    @Override
    public List<TopicResponseDto> getTopicsByUserId(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User not found", HttpStatus.NOT_FOUND));

        return topicMapper.toTopicResponseDtoList(user.getTopics().stream().toList());
    }

    @Override
    public List<TopicResponseDto> addTopicToUser(String userId, String topicId) {
        log.info("[User Service] Add topic {} to user {}", topicId, userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User not found", HttpStatus.NOT_FOUND));

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new TopicException("Topic not found", HttpStatus.NOT_FOUND));

        if (user.getTopics().contains(topic)) {
            throw new UserException("Topic already added to user", HttpStatus.CONFLICT);
        }

        user.getTopics().add(topic);
        userRepository.save(user);

        return topicMapper.toTopicResponseDtoList(user.getTopics().stream().toList());
    }

    @Override
    public List<TopicResponseDto> removeTopicFromUser(String userId, String topicId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User not found", HttpStatus.NOT_FOUND));

        user.getTopics().stream()
                .filter(topic -> topic.getId().equals(topicId))
                .findAny()
                .ifPresentOrElse(
                        topic -> user.getTopics().remove(topic),
                        () -> {
                            throw new TopicException("Topic not found", HttpStatus.NOT_FOUND);
                        });

        userRepository.save(user);

        return topicMapper.toTopicResponseDtoList(user.getTopics().stream().toList());
    }

}

package com.openclassrooms.mddapi.domains.user;

import com.openclassrooms.mddapi.application.authentication.AuthenticatedUserDto;
import com.openclassrooms.mddapi.domains.topic.TopicResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto getUser(@PathVariable String userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto updateUser(
            @PathVariable String userId,
            @Valid @RequestBody UserRequestDto userRequestDto,
            Authentication authentication) {
        return userService.updateUser(userId, userRequestDto, ((AuthenticatedUserDto) authentication.getPrincipal()).getUserId());
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto partialUpdateUser(
            @PathVariable String userId,
            @RequestBody Map<String, String> updatedFields,
            Authentication authentication) {
        return userService.partialUpdateUser(userId, updatedFields, ((AuthenticatedUserDto) authentication.getPrincipal()).getUserId());
    }

    @GetMapping("/{userId}/topics")
    public List<TopicResponseDto> getUserTopics(@PathVariable String userId) {
        return userService.getTopicsByUserId(userId);
    }

    @PutMapping("/{userId}/topics/{topicId}")
    public List<TopicResponseDto> addTopicToUser(@PathVariable String userId, @PathVariable String topicId) {
        return userService.addTopicToUser(userId, topicId);
    }

    @DeleteMapping("/{userId}/topics/{topicId}")
    public List<TopicResponseDto> removeTopicFromUser(@PathVariable String userId, @PathVariable String topicId) {
        return userService.removeTopicFromUser(userId, topicId);
    }

}

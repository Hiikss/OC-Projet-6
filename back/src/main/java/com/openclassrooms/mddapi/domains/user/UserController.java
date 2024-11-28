package com.openclassrooms.mddapi.domains.user;

import com.openclassrooms.mddapi.application.authentication.AuthenticatedUserDto;
import com.openclassrooms.mddapi.domains.topic.TopicResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return userService.updateUser(userId, userRequestDto, ((AuthenticatedUserDto) authentication).getId());
    }

    @GetMapping("/{userId}/topics")
    public List<TopicResponseDto> getUserTopics(@PathVariable String userId) {
        return List.of();
    }

    @PostMapping("/{userId}/topics/{topicId}")
    public void addTopicToUser(@PathVariable String userId, @PathVariable String topicId) {

    }

}

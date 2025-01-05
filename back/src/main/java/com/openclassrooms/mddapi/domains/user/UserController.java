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

    /**
     * Get an user by an id. Return 404 if not found
     * @param userId the user id
     * @return the user
     */
    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto getUser(@PathVariable String userId) {
        return userService.getUserById(userId);
    }

    /**
     * Update an user. Return 404 if user not found. Return 403 if the authenticated user is not the updated user.
     * Return 409 if the email or username is already used by another user.
     * @param userId userId
     * @param userRequestDto the user request body
     * @param authentication
     * @return the updated user
     */
    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto updateUser(
            @PathVariable String userId,
            @Valid @RequestBody UserRequestDto userRequestDto,
            Authentication authentication) {
        return userService.updateUser(userId, userRequestDto, ((AuthenticatedUserDto) authentication.getPrincipal()).getUserId());
    }

    /**
     * Update partially an user. Return 403 if the authenticated user is not the updated user.
     * Return 409 if the email or username is already used by another user
     * @param userId the user id
     * @param updatedFields the fields map
     * @param authentication
     * @return the updated user
     */
    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto partialUpdateUser(
            @PathVariable String userId,
            @RequestBody Map<String, String> updatedFields,
            Authentication authentication) {
        return userService.partialUpdateUser(userId, updatedFields, ((AuthenticatedUserDto) authentication.getPrincipal()).getUserId());
    }

    /**
     * Get user's topics
     * @param userId the user id
     * @return user's topics list
     */
    @GetMapping("/{userId}/topics")
    public List<TopicResponseDto> getUserTopics(@PathVariable String userId) {
        return userService.getTopicsByUserId(userId);
    }

    /**
     * Add a topic to an user. Return 404 if no user or topic found. Return 409 if user already has topic
     * @param userId the user id
     * @param topicId the topic id
     * @return the updated user's topics list
     */
    @PutMapping("/{userId}/topics/{topicId}")
    public List<TopicResponseDto> addTopicToUser(@PathVariable String userId, @PathVariable String topicId) {
        return userService.addTopicToUser(userId, topicId);
    }

    /**
     * Remove a topic from an user. Return 404 if no user found or topic not found for this user
     * @param userId the user id
     * @param topicId the topic id
     * @return the updated user's topics list
     */
    @DeleteMapping("/{userId}/topics/{topicId}")
    public List<TopicResponseDto> removeTopicFromUser(@PathVariable String userId, @PathVariable String topicId) {
        return userService.removeTopicFromUser(userId, topicId);
    }

}

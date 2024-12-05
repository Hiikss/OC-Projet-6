package com.openclassrooms.mddapi.domains.user;

import com.openclassrooms.mddapi.domains.topic.TopicResponseDto;

import java.util.List;

public interface UserService {

    UserResponseDto getUserById(String userId);

    UserResponseDto updateUser(String userId, UserRequestDto userRequestDto, String authenticatedUserId);

    List<TopicResponseDto> getTopicsByUserId(String userId);

    List<TopicResponseDto> addTopicToUser(String userId, String topicId);

    List<TopicResponseDto> removeTopicFromUser(String userId, String topicId);
}

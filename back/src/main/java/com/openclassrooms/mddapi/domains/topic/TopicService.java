package com.openclassrooms.mddapi.domains.topic;

import org.springframework.data.domain.Page;

import java.util.List;

public interface TopicService {

    Page<TopicResponseDto> getTopicsByPagination(int page, int size);

    List<TopicResponseDto> getTopicsByUserId(String userId);

    List<TopicResponseDto> addTopicToUser(String userId, String topicId);

    List<TopicResponseDto> removeTopicFromUser(String userId, String topicId);

}

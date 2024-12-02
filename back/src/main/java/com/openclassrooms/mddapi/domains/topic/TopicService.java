package com.openclassrooms.mddapi.domains.topic;

import org.springframework.data.domain.Page;

public interface TopicService {

    void createTopic(TopicRequestDto topicRequestDto);

    Page<TopicResponseDto> getTopicsByPagination(int page, int size);

}

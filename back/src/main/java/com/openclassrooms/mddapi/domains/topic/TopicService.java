package com.openclassrooms.mddapi.domains.topic;

import org.springframework.data.domain.Page;

public interface TopicService {

    Page<TopicResponseDto> getTopics(int page, int size);

}

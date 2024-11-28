package com.openclassrooms.mddapi.domains.topic;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;

    @Override
    public Page<TopicResponseDto> getTopics(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Topic> topics = topicRepository.findAll(pageable);
        List<TopicResponseDto> topicResponseDtos = topicMapper.toTopicResponseDtoList(topics.getContent());

        return new PageImpl<>(topicResponseDtos, pageable, topics.getTotalElements());
    }

}

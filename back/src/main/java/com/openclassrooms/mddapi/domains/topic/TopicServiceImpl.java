package com.openclassrooms.mddapi.domains.topic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;

    @Override
    public void createTopic(TopicRequestDto topicRequestDto) {
        log.info("[Topic Service] Creating topic");

        Optional<Topic> oTopic = topicRepository.findByTitle(topicRequestDto.title());

        if (oTopic.isPresent()) {
            throw new TopicException("Topic title already exists", HttpStatus.CONFLICT);
        }

        topicRepository.save(topicMapper.toTopic(topicRequestDto));
    }

    @Override
    public Page<TopicResponseDto> getTopicsByPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("title"));

        Page<Topic> topics = topicRepository.findAll(pageable);
        List<TopicResponseDto> topicResponseDtos = topicMapper.toTopicResponseDtoList(topics.getContent());

        return new PageImpl<>(topicResponseDtos, pageable, topics.getTotalElements());
    }

}

package com.openclassrooms.mddapi.domains.topic;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TopicMapper {

    TopicResponseDto toTopicResponseDto(Topic topic);

    List<TopicResponseDto> toTopicResponseDtoList(List<Topic> topics);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "posts", ignore = true)
    Topic toTopic(TopicRequestDto topicRequestDto);
}

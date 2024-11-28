package com.openclassrooms.mddapi.domains.topic;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TopicMapper {

    TopicResponseDto toTopicResponseDto(Topic topic);

    List<TopicResponseDto> toTopicResponseDtoList(List<Topic> topics);
}

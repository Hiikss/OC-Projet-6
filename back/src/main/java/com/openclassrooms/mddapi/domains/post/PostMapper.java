package com.openclassrooms.mddapi.domains.post;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "authorUsername", source = "author.username")
    @Mapping(target = "topicTitle", source = "topic.title")
    PostResponseDto toPostResponseDto(Post post);

    List<PostResponseDto> toPostResponseDtoList(List<Post> posts);
}

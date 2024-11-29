package com.openclassrooms.mddapi.domains.post;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {

    PostResponseDto toPostResponseDto(Post post);

    List<PostResponseDto> toPostResponseDtoList(List<Post> posts);
}

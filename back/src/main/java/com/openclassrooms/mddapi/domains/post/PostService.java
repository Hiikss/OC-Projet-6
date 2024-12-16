package com.openclassrooms.mddapi.domains.post;

import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {

    Page<PostResponseDto> getPostsByPagination(int page, int size, List<String> topicTitles);

    PostResponseDto getPostById(String postId);

    void createPost(PostRequestDto postRequestDto, String authorId);
}

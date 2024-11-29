package com.openclassrooms.mddapi.domains.post;

import org.springframework.data.domain.Page;

public interface PostService {

    Page<PostResponseDto> getPostsByPagination(int page, int size);

    PostResponseDto getPostById(String postId);

    void createPost(PostRequestDto postRequestDto, String authorId);
}

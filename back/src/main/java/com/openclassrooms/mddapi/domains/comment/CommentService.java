package com.openclassrooms.mddapi.domains.comment;

import java.util.List;

public interface CommentService {

    List<CommentResponseDto> getCommentsByPostId(String postId);

    void createComment(CommentRequestDto commentRequestDto, String postId, String authorId);
}

package com.openclassrooms.mddapi.domains.post.comment;

import java.time.LocalDateTime;

public record CommentResponseDto(String content, String authorUsername, LocalDateTime createdAt) {
}

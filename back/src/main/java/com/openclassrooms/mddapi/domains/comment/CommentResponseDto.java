package com.openclassrooms.mddapi.domains.comment;

import java.time.LocalDateTime;

public record CommentResponseDto(String content, String authorUsername, LocalDateTime createdAt) {
}

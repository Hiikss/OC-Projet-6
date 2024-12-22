package com.openclassrooms.mddapi.domains.post;

import java.time.LocalDateTime;

public record PostResponseDto(
        String id,
        String title,
        String content,
        LocalDateTime createdAt,
        String authorUsername,
        String topicTitle) {
}

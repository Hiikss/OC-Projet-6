package com.openclassrooms.mddapi.domains.post;

public record PostResponseDto(
        String id,
        String title,
        String content,
        String createdAt,
        String authorUsername,
        String topicTitle) {
}

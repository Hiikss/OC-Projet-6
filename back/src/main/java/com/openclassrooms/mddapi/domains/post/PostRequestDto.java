package com.openclassrooms.mddapi.domains.post;

public record PostRequestDto(
        String title,
        String content,
        String topicId
) {
}

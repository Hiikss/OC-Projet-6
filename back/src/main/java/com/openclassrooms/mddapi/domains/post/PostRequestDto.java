package com.openclassrooms.mddapi.domains.post;

import jakarta.validation.constraints.NotBlank;

public record PostRequestDto(

        @NotBlank
        String title,

        @NotBlank
        String content,

        @NotBlank
        String topicId
) {
}

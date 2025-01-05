package com.openclassrooms.mddapi.domains.comment;

import jakarta.validation.constraints.NotBlank;

public record CommentRequestDto(@NotBlank String content) {
}

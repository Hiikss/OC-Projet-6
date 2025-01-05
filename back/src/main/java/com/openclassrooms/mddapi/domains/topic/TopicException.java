package com.openclassrooms.mddapi.domains.topic;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TopicException extends RuntimeException {

    private final HttpStatus status;

    public TopicException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}

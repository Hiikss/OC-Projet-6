package com.openclassrooms.mddapi.application.errors;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.openclassrooms.mddapi.application.authentication.AuthException;
import com.openclassrooms.mddapi.domains.post.PostException;
import com.openclassrooms.mddapi.domains.topic.TopicException;
import com.openclassrooms.mddapi.domains.user.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, List<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return Map.of(
                "errors",
                e.getBindingResult().getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage)
                        .toList()
        );
    }

    @ExceptionHandler(JWTVerificationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDto handleJWTVerificationException(JWTVerificationException e) {
        return new ErrorDto(e.getMessage());
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorDto> handleAuthException(AuthException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorDto> handleUserException(UserException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(TopicException.class)
    public ResponseEntity<ErrorDto> handleTopicException(TopicException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(PostException.class)
    public ResponseEntity<ErrorDto> handlePostException(PostException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(new ErrorDto(e.getMessage()));
    }
}

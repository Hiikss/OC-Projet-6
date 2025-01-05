package com.openclassrooms.mddapi.domains.post;

import com.openclassrooms.mddapi.application.authentication.AuthenticatedUserDto;
import com.openclassrooms.mddapi.domains.comment.CommentRequestDto;
import com.openclassrooms.mddapi.domains.comment.CommentResponseDto;
import com.openclassrooms.mddapi.domains.comment.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Validated
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    /**
     * Return a list of post based on pagination and filter. The total count of posts is returned in a X-Total-Count header
     * @param page the page number (default is 1)
     * @param size the page size (default is 10)
     * @param topicTitles the topics titles list filter
     * @param sortBy the sorting field (default is createdAt)
     * @param sortDir the sorting direction (default is desc)
     * @return the list of posts
     */
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") List<String> topicTitles,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
            ) {
        List<String> decodedTitles = topicTitles.stream()
                .map(title -> URLDecoder.decode(title, StandardCharsets.UTF_8))
                .toList();
        Page<PostResponseDto> postsPage = postService.getPostsByPagination(page, size, decodedTitles, sortBy, sortDir);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", Long.toString(postsPage.getTotalElements()));
        headers.setAccessControlExposeHeaders(List.of("X-Total-Count"));

        return ResponseEntity.ok().headers(headers).body(postsPage.getContent());
    }

    /**
     * Get a post by an id. Return 404 if not found
     * @param postId the post id
     * @return the post
     */
    @GetMapping("/{postId}")
    public PostResponseDto getPostById(@PathVariable String postId) {
        return postService.getPostById(postId);
    }

    /**
     * Create a new post
     * @param postRequestDto the post dto
     * @param authentication
     * @return the created post
     */
    @PostMapping
    public PostResponseDto createPost(@Valid @RequestBody PostRequestDto postRequestDto, Authentication authentication) {
        return postService.createPost(postRequestDto, ((AuthenticatedUserDto) authentication.getPrincipal()).getUserId());
    }

    /**
     * Get comments for a post
     * @param postId the post id
     * @return the list of comments
     */
    @GetMapping("/{postId}/comments")
    public List<CommentResponseDto> getCommentsByPostId(@PathVariable String postId) {
        return commentService.getCommentsByPostId(postId);
    }

    /**
     * Create a comment for a post
     * @param commentRequestDto the comment dto
     * @param postId the post id
     * @param authentication
     */
    @PostMapping("/{postId}/comments")
    public void addCommentToPost(
            @Valid @RequestBody CommentRequestDto commentRequestDto,
            @PathVariable String postId,
            Authentication authentication) {
        commentService.createComment(commentRequestDto, postId, ((AuthenticatedUserDto) authentication.getPrincipal()).getUserId());
    }
}

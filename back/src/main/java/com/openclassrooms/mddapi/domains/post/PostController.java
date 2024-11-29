package com.openclassrooms.mddapi.domains.post;

import com.openclassrooms.mddapi.application.authentication.AuthenticatedUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts(@RequestParam int page, @RequestParam int size) {
        Page<PostResponseDto> postsPage = postService.getPostsByPagination(page, size);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", Long.toString(postsPage.getTotalElements()));
        headers.setAccessControlExposeHeaders(List.of("X-Total-Count"));

        return ResponseEntity.ok().headers(headers).body(postsPage.getContent());
    }

    @GetMapping("/{postId}")
    public PostResponseDto getPostById(@PathVariable String postId) {
        return postService.getPostById(postId);
    }

    @PostMapping
    public void createPost(@RequestBody PostRequestDto postRequestDto, Authentication authentication) {
        postService.createPost(postRequestDto, ((AuthenticatedUserDto) authentication).getId());
    }

    @PostMapping("/{postId}/comments")
    public void addCommentToPost() {

    }
}

package com.openclassrooms.mddapi.domains.comment;

import com.openclassrooms.mddapi.domains.post.Post;
import com.openclassrooms.mddapi.domains.post.PostException;
import com.openclassrooms.mddapi.domains.post.PostRepository;
import com.openclassrooms.mddapi.domains.user.User;
import com.openclassrooms.mddapi.domains.user.UserException;
import com.openclassrooms.mddapi.domains.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    /**
     * Get comments for a post
     * @param postId
     * @return list of comments
     */
    @Override
    public List<CommentResponseDto> getCommentsByPostId(String postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return commentMapper.toCommentResponseDtoList(comments);
    }

    /**
     * Create a new comment for a post
     * @param commentRequestDto
     * @param postId
     * @param authorId
     */
    @Override
    public void createComment(CommentRequestDto commentRequestDto, String postId, String authorId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("Post not found", HttpStatus.NOT_FOUND));

        User user = userRepository.findById(authorId)
                .orElseThrow(() -> new UserException("User not found", HttpStatus.NOT_FOUND));

        Comment comment = Comment.builder()
                .content(commentRequestDto.content())
                .author(user)
                .post(post)
                .build();

        commentRepository.save(comment);
    }
}

package com.openclassrooms.mddapi.domains.post;

import com.openclassrooms.mddapi.domains.topic.Topic;
import com.openclassrooms.mddapi.domains.topic.TopicException;
import com.openclassrooms.mddapi.domains.topic.TopicRepository;
import com.openclassrooms.mddapi.domains.user.User;
import com.openclassrooms.mddapi.domains.user.UserException;
import com.openclassrooms.mddapi.domains.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;

    @Override
    public Page<PostResponseDto> getPostsByPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());

        Page<Post> posts = postRepository.findAll(pageable);
        List<PostResponseDto> postResponseDtos = postMapper.toPostResponseDtoList(posts.getContent());

        return new PageImpl<>(postResponseDtos, pageable, posts.getTotalElements());
    }

    @Override
    public PostResponseDto getPostById(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("Post not found", HttpStatus.NOT_FOUND));

        return postMapper.toPostResponseDto(post);
    }

    @Override
    public void createPost(PostRequestDto postRequestDto, String authorId) {
        User user = userRepository.findById(authorId)
                .orElseThrow(() -> new UserException("User not found", HttpStatus.NOT_FOUND));

        Topic topic = topicRepository.findById(postRequestDto.topicId())
                .orElseThrow(() -> new TopicException("Topic not found", HttpStatus.NOT_FOUND));

        Post post = Post.builder()
                .title(postRequestDto.title())
                .content(postRequestDto.content())
                .author(user)
                .topic(topic).build();

        postRepository.save(post);
    }
}

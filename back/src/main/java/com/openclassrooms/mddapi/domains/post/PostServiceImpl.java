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

    /**
     * Get post based on pagination and filters
     * @param page the page number (1 is the first page)
     * @param size the page size
     * @param topicTitles the topic titles list
     * @param sortBy the sort field (e.g createAt)
     * @param sortDir the sort direction (either asc or desc)
     * @return the page of posts
     */
    @Override
    public Page<PostResponseDto> getPostsByPagination(int page, int size, List<String> topicTitles, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<Post> posts = postRepository.findAllByTopicTitles(topicTitles, pageable);
        List<PostResponseDto> postResponseDtos = postMapper.toPostResponseDtoList(posts.getContent());

        return new PageImpl<>(postResponseDtos, pageable, posts.getTotalElements());
    }

    /**
     * Get a post by its id
     * @param postId
     * @return the post
     */
    @Override
    public PostResponseDto getPostById(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("Post not found", HttpStatus.NOT_FOUND));

        return postMapper.toPostResponseDto(post);
    }

    /**
     * Create a new post
     * @param postRequestDto
     * @param authorId
     * @return the created post
     */
    @Override
    public PostResponseDto createPost(PostRequestDto postRequestDto, String authorId) {
        User user = userRepository.findById(authorId)
                .orElseThrow(() -> new UserException("User not found", HttpStatus.NOT_FOUND));

        Topic topic = topicRepository.findById(postRequestDto.topicId())
                .orElseThrow(() -> new TopicException("Topic not found", HttpStatus.NOT_FOUND));

        Post post = Post.builder()
                .title(postRequestDto.title())
                .content(postRequestDto.content())
                .author(user)
                .topic(topic).build();

        return postMapper.toPostResponseDto(postRepository.save(post));
    }
}

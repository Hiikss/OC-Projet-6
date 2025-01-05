package com.openclassrooms.mddapi.domains.topic;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    /**
     * Create a new topic
     * @param topicRequestDto
     */
    @PostMapping
    public void createTopic(@RequestBody TopicRequestDto topicRequestDto) {
        topicService.createTopic(topicRequestDto);
    }

    /**
     * Get the topic list based on pagination. Return the total count in a X-Total-Count header
     * @param page the page number (default is 1)
     * @param size the page size (default is 10)
     * @return the list of topics
     */
    @GetMapping
    public ResponseEntity<List<TopicResponseDto>> getTopics(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<TopicResponseDto> topicsPage = topicService.getTopicsByPagination(page, size);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", Long.toString(topicsPage.getTotalElements()));
        headers.setAccessControlExposeHeaders(List.of("X-Total-Count"));

        return ResponseEntity.ok().headers(headers).body(topicsPage.getContent());
    }
}

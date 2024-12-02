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

    @PostMapping
    public void createTopic(@RequestBody TopicRequestDto topicRequestDto) {
        topicService.createTopic(topicRequestDto);
    }

    @GetMapping
    public ResponseEntity<List<TopicResponseDto>> getTopics(@RequestParam int page, @RequestParam int size) {
        Page<TopicResponseDto> topicsPage = topicService.getTopicsByPagination(page, size);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", Long.toString(topicsPage.getTotalElements()));
        headers.setAccessControlExposeHeaders(List.of("X-Total-Count"));

        return ResponseEntity.ok().headers(headers).body(topicsPage.getContent());
    }
}

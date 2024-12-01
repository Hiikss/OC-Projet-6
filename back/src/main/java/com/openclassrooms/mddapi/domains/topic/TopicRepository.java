package com.openclassrooms.mddapi.domains.topic;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, String> {

    List<Topic> findByUserId(String userId);
}

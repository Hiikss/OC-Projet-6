package com.openclassrooms.mddapi.domains.topic;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, String> {

    Optional<Topic> findByTitle(String title);
}

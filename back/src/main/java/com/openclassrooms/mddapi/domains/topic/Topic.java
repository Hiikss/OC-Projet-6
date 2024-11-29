package com.openclassrooms.mddapi.domains.topic;

import com.openclassrooms.mddapi.domains.post.Post;
import com.openclassrooms.mddapi.domains.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "topics")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Topic {

    @Id
    @UuidGenerator
    private String id;

    @Column(unique = true, nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @ManyToMany(mappedBy = "topics")
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "topic")
    private Set<Post> posts = new HashSet<>();

}

package com.openclassrooms.mddapi.domains.post.comment;

import com.openclassrooms.mddapi.domains.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "comments")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @UuidGenerator
    private String id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
}

package com.example.rateit.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * created by chethan on 21-12-2021
 **/

@Entity
@Table(name = "user_posts")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;
    private String content;
    private String mediaType;
    private int mediaId;

    private LocalDateTime createdAt;

    public Post(User user, String content, String mediaType, int mediaId, LocalDateTime createdAt) {
        this.user = user;
        this.content = content;
        this.mediaType = mediaType;
        this.mediaId = mediaId;
        this.createdAt = createdAt;
    }
}

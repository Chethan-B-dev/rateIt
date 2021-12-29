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
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    // TODO: fix posts not working for other users or other posts

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;

    private String content;
    private LocalDateTime createdAt;
    private int rating;

    private String mediaType;
    @Column(unique = true)
    private int mediaId;

    public Post(User user, String content, LocalDateTime createdAt, int rating, String mediaType, int mediaId) {
        this.user = user;
        this.content = content;
        this.createdAt = createdAt;
        this.rating = rating;
        this.mediaType = mediaType;
        this.mediaId = mediaId;
    }
}

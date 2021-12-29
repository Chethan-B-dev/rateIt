package com.example.rateit.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * created by chethan on 21-12-2021
 **/


@Entity
@Table(name = "user_watch_list")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WatchList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;

    private String mediaType;

    private int mediaId;

    private LocalDateTime addedAt;


    public WatchList(User user, String mediaType, int mediaId, LocalDateTime addedAt) {
        this.user = user;
        this.mediaType = mediaType;
        this.mediaId = mediaId;
        this.addedAt = addedAt;
    }
}

package com.example.rateit.model;

import lombok.*;

/**
 * created by chethan on 28-12-2021
 **/

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DisplayPost {
    private Post post;
    private Media media;
    private boolean isMyPost = false;
}

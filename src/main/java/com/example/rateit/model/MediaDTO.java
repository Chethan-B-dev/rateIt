package com.example.rateit.model;

import lombok.*;

/**
 * created by chethan on 30-12-2021
 **/

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MediaDTO {
    private Media media;
    private boolean isMyMedia = false;
}

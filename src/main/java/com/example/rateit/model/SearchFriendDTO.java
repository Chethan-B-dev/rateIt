package com.example.rateit.model;

import lombok.*;

/**
 * created by chethan on 31-12-2021
 **/

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchFriendDTO {
    private User user;
    private boolean isAccepted = false;
    private boolean isPending = false;
    private boolean isReceived = false;
}

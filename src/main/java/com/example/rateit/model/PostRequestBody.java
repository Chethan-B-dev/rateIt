package com.example.rateit.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * created by chethan on 21-12-2021
 **/

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestBody {
    @NotBlank(message = "content cannot be empty")
    private String content;
    private String mediaType;
    private String mediaId;
    private int rating;
}

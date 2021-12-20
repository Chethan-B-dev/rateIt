package com.example.rateit.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * created by chethan on 20-12-2021
 **/

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Review {
    @JsonProperty("author_details")
    private Author author;
    private String content;
    @JsonProperty("created_at")
    private String createdAt;
}

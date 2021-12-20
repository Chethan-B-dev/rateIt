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
public class Author {
    // "name":"","username":"tmdb44006625","avatar_path":null,"rating":9.0
    private String name;
    private String username;
    @JsonProperty("avatar_path")
    private String avatar;
    private double rating;
}

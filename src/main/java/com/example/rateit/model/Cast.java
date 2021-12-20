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
public class Cast {
    private String name;
    private String character;
    @JsonProperty("profile_path")
    private String profile;
}

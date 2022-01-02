package com.example.rateit.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * created by chethan on 26-12-2021
 **/

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Media implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    @JsonAlias("name")
    private String title;
    @JsonProperty("backdrop_path")
    private String backdrop;
    private String overview;
    @JsonProperty("vote_average")
    private double userRating;
    @JsonProperty("poster_path")
    private String poster;
    @JsonProperty("original_language")
    private String language;
    @JsonProperty("media_type")
    private String mediaType;
    private List<Genre> genres;
    @JsonProperty("release_date")
    @JsonAlias("first_air_date")
    private LocalDate releaseDate;
    @JsonProperty("homepage")
    private String link;

}

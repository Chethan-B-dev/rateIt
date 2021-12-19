package com.example.rateit.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * created by chethan on 19-12-2021
 **/

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie implements Serializable {
    private int id;
    private String title;
    private String overview;
    @JsonProperty("original_language")
    private String langugae;
    @JsonProperty("release_date")
    private LocalDate releaseDate;
    private List<Genre> genres;
    @JsonProperty("poster_path")
    private String poster;
    @JsonProperty("backdrop_path")
    private String backdrop;
    @JsonProperty("vote_average")
    private double userRating;
    private int runtime;

}

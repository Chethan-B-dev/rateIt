package com.example.rateit.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * created by chethan on 26-12-2021
 **/

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie extends Media {

    private int runtime;

    public Movie(int id, String title, String backdrop, String overview, double userRating, String poster, String language, List<Genre> genres, LocalDate releaseDate, int runtime) {
        super(id, title, backdrop, overview, userRating, poster, language, "movie", genres, releaseDate);
        this.runtime = runtime;
    }
}

package com.example.rateit.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class TV extends Media {

    @JsonProperty("episode_run_time")
    private List<Integer> episodeRuntime;
    @JsonProperty("number_of_seasons")
    private int NumberOfSeasons;
    @JsonProperty("number_of_episodes")
    private int NumberOfEpisodes;

    public TV(int id, String title, String backdrop, String overview, double userRating, String poster, String language, List<Genre> genres, LocalDate releaseDate, List<Integer> episodeRuntime, int numberOfSeasons, int numberOfEpisodes) {
        super(id, title, backdrop, overview, userRating, poster, language, "tv", genres, releaseDate);
        this.episodeRuntime = episodeRuntime;
        NumberOfSeasons = numberOfSeasons;
        NumberOfEpisodes = numberOfEpisodes;
    }
}

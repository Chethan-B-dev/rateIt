package com.example.rateit.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * created by chethan on 19-12-2021
 **/
public class TV {
    private Long id;
    private String title;
    private String overview;
    private String langugae;
    private LocalDateTime releaseDate;
    private List<Genre> genres;
    private String poster;
    private String backdrop;
    private int userRating;
    private int episodeRuntime;
    private int NumberOfSeasons;
    private int NumberOfEpisodes;
}

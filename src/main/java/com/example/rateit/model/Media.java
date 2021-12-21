package com.example.rateit.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * created by chethan on 19-12-2021
 **/

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Media implements Serializable {
    private int id;
    private String title;
    private String name;
    private String overview;
    @JsonProperty("original_language")
    private String langugae;
    @JsonProperty("release_date")
    private LocalDate releaseDate;
    @JsonProperty("first_air_date")
    private LocalDate firstAirDate;
    @JsonProperty("poster_path")
    private String poster;
    @JsonProperty("media_type")
    private String mediaType;

    public Media(String title, String name, String overview, String langugae, LocalDate releaseDate, LocalDate firstAirDate, String poster, String mediaType) {
        this.title = title;
        this.name = name;
        this.overview = overview;
        this.langugae = langugae;
        this.releaseDate = releaseDate;
        this.firstAirDate = firstAirDate;
        this.poster = poster;
        this.mediaType = mediaType;
    }
}

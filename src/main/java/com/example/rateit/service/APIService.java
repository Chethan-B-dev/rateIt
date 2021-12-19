package com.example.rateit.service;

import com.example.rateit.model.Movie;
import com.example.rateit.model.TV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * created by chethan on 19-12-2021
 **/
@Service
public class APIService {

    private final String url = "https://api.themoviedb.org/3/";
    private final String apiKey = "fc1f6677d898408bfc0966f089fc8088";
    private static final Logger log = LoggerFactory.getLogger(APIService.class);

    public Movie getMovie(int id){
        RestTemplate restTemplate = new RestTemplate();
        String movieUrl = url + "movie/" + id + String.format("?api_key=%s&language=en-US",apiKey);
        return restTemplate.getForObject(movieUrl, Movie.class);
    }

    public TV getTV(int id){
        RestTemplate restTemplate = new RestTemplate();
        String movieUrl = url + "tv/" + id + String.format("?api_key=%s&language=en-US",apiKey);
        return restTemplate.getForObject(movieUrl, TV.class);
    }
}

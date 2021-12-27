package com.example.rateit.controller;

import com.example.rateit.model.Media;
import com.example.rateit.service.APIService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * created by chethan on 19-12-2021
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/api/")
public class APIController {

    private final APIService apiService;

    @GetMapping("/movie/{id}")
    private Media getMovie(@PathVariable int id){
        return apiService.getMovie(id);
    }

    @GetMapping("/tv/{id}")
    private Media getTV(@PathVariable int id){
        return apiService.getTV(id);
    }

    @GetMapping("/movie/trending")
    private List<Media> getTrendingMovies() throws JsonProcessingException {
        return apiService.getTrendingMovies();
    }

    @GetMapping("/tv/trending")
    private List<Media> getTrendingTV() throws JsonProcessingException {
        return apiService.getTrendingTV();
    }

    @GetMapping("/search/{term}")
    private List<Media> search(@PathVariable String term) throws JsonProcessingException {
        return apiService.search(term);
    }
}

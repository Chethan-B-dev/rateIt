package com.example.rateit.controller;

import com.example.rateit.model.Movie;
import com.example.rateit.model.TV;
import com.example.rateit.service.APIService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * created by chethan on 19-12-2021
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/api/")
public class APIController {

    private final APIService apiService;

    @GetMapping("/movie/{id}")
    private Movie getMovie(@PathVariable int id){
        return apiService.getMovie(id);
    }

    @GetMapping("/tv/{id}")
    private TV getTV(@PathVariable int id){
        return apiService.getTV(id);
    }
}

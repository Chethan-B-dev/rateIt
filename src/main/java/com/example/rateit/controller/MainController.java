package com.example.rateit.controller;

import com.example.rateit.model.Movie;
import com.example.rateit.model.TV;
import com.example.rateit.service.APIService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * created by chethan on 19-12-2021
 **/
@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private APIService apiService;

    @GetMapping
    public ModelAndView home() throws JsonProcessingException {
        ModelAndView mav = new ModelAndView("index");
        List<Movie> trendingMovies = apiService.getTrendingMovies();
        List<TV> trendingTv = apiService.getTrendingTV();
        System.out.println(trendingTv);
        mav.addObject("trendingMovies",trendingMovies);
        mav.addObject("trendingTv",trendingTv);
        return mav;
    }
}


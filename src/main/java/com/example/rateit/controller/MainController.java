package com.example.rateit.controller;

import com.example.rateit.model.*;
import com.example.rateit.service.APIService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        mav.addObject("trendingMovies",trendingMovies);
        mav.addObject("trendingTv",trendingTv);
        return mav;
    }

    @GetMapping("/search")
    public ModelAndView search(@RequestParam String query) throws JsonProcessingException {
        ModelAndView mav = new ModelAndView("search");
        List<Media> mediaList = apiService.search(query);
        mav.addObject("search",query);
        mav.addObject("mediaList",mediaList);
        return mav;
    }

    @GetMapping("/movie/{id}")
    public ModelAndView movie(@PathVariable int id) throws JsonProcessingException {
        ModelAndView mav = new ModelAndView("movie");
        Movie movie = apiService.getMovie(id);
        if (movie == null) return new ModelAndView("redirect:/?error");
        List movieDetails = apiService.getMovieCastReviewsAndSimilarMovies(id);
        List<Cast> casts = (List<Cast>) movieDetails.get(0);
        List<Movie> similarMovies = (List<Movie>) movieDetails.get(1);
        List<Review> movieReviews = (List<Review>) movieDetails.get(2);
        StringBuilder genreBuilder = new StringBuilder();
        for (Genre genre: movie.getGenres()) {
            genreBuilder.append(genre.getName()).append(" ,");
        }
        mav.addObject("movie",movie);
        mav.addObject("casts",casts);
        mav.addObject("similarMovies",similarMovies);
        mav.addObject("movieReviews",movieReviews);
        mav.addObject("numberOfReviews",movieReviews.size());
        mav.addObject("genres",genreBuilder.substring(0,genreBuilder.length() - 1));
        return mav;
    }

    @GetMapping("/tv/{id}")
    public ModelAndView tv(@PathVariable int id) throws JsonProcessingException {
        ModelAndView mav = new ModelAndView("tv");
        TV tv = apiService.getTV(id);
        mav.addObject("tv",tv);
        return mav;
    }

    @GetMapping("/list/movie/popular")
    public ModelAndView getPopularMovies() throws JsonProcessingException {
        ModelAndView mav = new ModelAndView("list");
        List<Movie> movieList = apiService.getPopularMovies();
        mav.addObject("movieList",movieList);
        mav.addObject("topic","Popular Movies");
        return mav;
    }

    @GetMapping("/list/movie/upcoming")
    public ModelAndView getUpcomingMovies() throws JsonProcessingException {
        ModelAndView mav = new ModelAndView("list");
        List<Movie> movieList = apiService.getUpcomingMovies();
        mav.addObject("movieList",movieList);
        mav.addObject("topic","Upcoming Movies");
        return mav;
    }

    @GetMapping("/list/tv/top")
    public ModelAndView getTopRatedTv() throws JsonProcessingException {
        ModelAndView mav = new ModelAndView("list");
        List<TV> tvList = apiService.getTopRatedTV();
        mav.addObject("tvList",tvList);
        mav.addObject("topic","Top Rated TV Shows");
        return mav;
    }

    @GetMapping("/list/tv/air")
    public ModelAndView getOnAirTv() throws JsonProcessingException {
        ModelAndView mav = new ModelAndView("list");
        List<TV> tvList = apiService.getOnAirTV();
        mav.addObject("tvList",tvList);
        mav.addObject("topic","On Air TV Shows");
        return mav;
    }

}


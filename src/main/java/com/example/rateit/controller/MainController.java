package com.example.rateit.controller;

import com.example.rateit.model.*;
import com.example.rateit.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;

/**
 * created by chethan on 19-12-2021
 **/
@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private APIService apiService;
    @Autowired
    private UserService userService;
    @Autowired
    private ListService listService;
    @Autowired
    private PostService postService;



    @GetMapping
    public ModelAndView home() throws JsonProcessingException {
        ModelAndView mav = new ModelAndView("index");
        List<Movie> trendingMovies = apiService.getTrendingMovies();
        List<TV> trendingTv = apiService.getTrendingTV();
        mav.addObject("trendingMovies",trendingMovies);
        mav.addObject("trendingTv",trendingTv);
        mav.addObject("user",new User());
        return mav;
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userService.save(user);
        return "redirect:/?sucess-register";
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
    public ModelAndView movie(@PathVariable int id,@AuthenticationPrincipal MyUserDetails myUserDetails) throws JsonProcessingException {
        ModelAndView mav = new ModelAndView("movie");
        Movie movie = apiService.getMovie(id);
        if (movie == null) return new ModelAndView("redirect:/?error");
        List movieDetails = apiService.getMovieCastReviewsAndSimilarMovies(id);
        List<Cast> casts = (List<Cast>) movieDetails.get(0);
        List<Movie> similarMovies = (List<Movie>) movieDetails.get(1);
        List<Review> movieReviews = (List<Review>) movieDetails.get(2);
        try{
            User user = myUserDetails.getUser();
            boolean hasWatched = listService.hasWatched(user.getId(),id);
            boolean hasWished = listService.hasWished(user.getId(),id);
            System.out.println(hasWatched + " " + hasWished);
            mav.addObject("hasWatched",hasWatched);
            mav.addObject("hasWished",hasWished);
        }catch (NullPointerException ex){
            System.out.println("user not logged in from /movie/id");
        }
        mav.addObject("movie",movie);
        mav.addObject("customPost",new PostRequestBody());
        mav.addObject("casts",casts);
        mav.addObject("similarMovies",similarMovies);
        mav.addObject("movieReviews",movieReviews);
        mav.addObject("numberOfReviews",movieReviews.size());
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

    @GetMapping("/list/movie/top")
    public ModelAndView getTopRatedMovies() throws JsonProcessingException {
        ModelAndView mav = new ModelAndView("list");
        List<Movie> movieList = apiService.getTopRatedMovies();
        mav.addObject("movieList",movieList);
        mav.addObject("topic","Top Rated Movies");
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

    @GetMapping("/watchlist/{media}/{id}")
    public ModelAndView addMediaToWatchList(@PathVariable String media,
                                            @PathVariable int id,
                                            @AuthenticationPrincipal MyUserDetails myUserDetails){
        User user;

        try {
            user = myUserDetails.getUser();
        } catch (NullPointerException ex){
            return new ModelAndView("redirect:/" + media + "/" + id);
        }
        WatchList newMedia = new WatchList(user,media,id);
        listService.saveWatchList(newMedia);
        return new ModelAndView("redirect:/" + media + "/" + id);
    }

    @GetMapping("/wishlist/{media}/{id}")
    public ModelAndView addMediaToWishList(@PathVariable String media,
                                            @PathVariable int id,
                                            @AuthenticationPrincipal MyUserDetails myUserDetails){
        User user;

        try {
            user = myUserDetails.getUser();
        } catch (NullPointerException ex){
            return new ModelAndView("redirect:/" + media + "/" + id);
        }

        WishList newMedia = new WishList(user,media,id);
        listService.saveWishList(newMedia);
        return new ModelAndView("redirect:/" + media + "/" + id);
    }

    @GetMapping("/mywatchlist")
    public ModelAndView myWatchList(@AuthenticationPrincipal MyUserDetails myUserDetails){
        User user;
        try {
            user = myUserDetails.getUser();
        } catch (NullPointerException ex){
            return new ModelAndView("redirect:/");
        }
        List<Media> mediaList = listService.getWatchListMedia(user.getId());
        ModelAndView mav = new ModelAndView("media_list");
        mav.addObject("mediaList",mediaList);
        mav.addObject("topic","My WatchList");
        return mav;
    }

    @GetMapping("/mywishlist")
    public ModelAndView myWishList(@AuthenticationPrincipal MyUserDetails myUserDetails){
        User user;
        try {
            user = myUserDetails.getUser();
        } catch (NullPointerException ex){
            return new ModelAndView("redirect:/");
        }
        List<Media> mediaList = listService.getWishListMedia(user.getId());
        ModelAndView mav = new ModelAndView("media_list");
        mav.addObject("mediaList",mediaList);
        mav.addObject("topic","My WishList");
        return mav;
    }

    @PostMapping("/addPost")
    public ModelAndView addPost(@ModelAttribute PostRequestBody postRequestBody,
                                @AuthenticationPrincipal MyUserDetails myUserDetails){
        User user;
        try {
            user = myUserDetails.getUser();
        } catch (NullPointerException ex){
            return new ModelAndView("redirect:/");
        }
        Post post = new Post(
                user,
                postRequestBody.getContent(),
                postRequestBody.getMediaType(),
                Integer.parseInt(postRequestBody.getMediaId()),
                LocalDateTime.now()
        );
        postService.save(post);
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/myposts")
    public ModelAndView myPosts(@AuthenticationPrincipal MyUserDetails myUserDetails){
        User user;
        try {
            user = myUserDetails.getUser();
        } catch (NullPointerException ex){
            return new ModelAndView("redirect:/");
        }
        List<Media> mediaList = apiService.getPostsOfUser(user.getId());
        ModelAndView mav = new ModelAndView("post");
        mav.addObject("mediaList",mediaList);
        mav.addObject("username",user.getUsername());
        return mav;
    }
}


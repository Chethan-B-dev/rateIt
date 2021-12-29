package com.example.rateit.controller;

import com.example.rateit.MyUtilities;
import com.example.rateit.model.*;
import com.example.rateit.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.ArrayList;
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


    @ModelAttribute("user")
    public User getUser(){
        return new User();
    }

    @GetMapping
    public ModelAndView home() throws JsonProcessingException {
        ModelAndView mav = new ModelAndView("index");
        List<Media> trendingMovies = apiService.getTrendingMovies();
        List<Media> trendingTv = apiService.getTrendingTV();
        mav.addObject("trendingMovies",trendingMovies);
        mav.addObject("trendingTv",trendingTv);
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
        Media movie = apiService.getMovie(id);
        if (movie == null) return new ModelAndView("redirect:/?error");
        List<Cast> casts = apiService.getMediaCast(id,"movie");
        List<Media> similarMovies = apiService.getSimilarMedia(id,"movie");
        List<Review> movieReviews = apiService.getMediaReviews(id,"movie");
        try{
            User user = myUserDetails.getUser();
            boolean hasWatched = listService.hasWatched(user.getId(),id);
            boolean hasWished = listService.hasWished(user.getId(),id);
            boolean hasPosted = postService.hasPosted(user.getId(), id);
            mav.addObject("hasWatched",hasWatched);
            mav.addObject("hasWished",hasWished);
            mav.addObject("hasPosted",hasPosted);
        }catch (NullPointerException ex){
            System.out.println("user not logged in from /movie/id");
        }
        mav.addObject("movie",movie);
        mav.addObject("runtime", MyUtilities.minToHours(((Movie) movie).getRuntime()));
        mav.addObject("releaseYear",movie.getReleaseDate().getYear());
        mav.addObject("customPost",new PostRequestBody());
        mav.addObject("casts",casts);
        mav.addObject("similarMovies",similarMovies);
        mav.addObject("movieReviews",movieReviews);
        mav.addObject("isReviewsEmpty",movieReviews.isEmpty());
        mav.addObject("numberOfReviews",movieReviews.size());
        return mav;
    }

    @GetMapping("/tv/{id}")
    public ModelAndView tv(@PathVariable int id,@AuthenticationPrincipal MyUserDetails myUserDetails) throws JsonProcessingException {
        ModelAndView mav = new ModelAndView("tv");
        Media tv = apiService.getTV(id);
        if (tv == null) return new ModelAndView("redirect:/?error");
        List<Cast> casts = apiService.getMediaCast(id,"tv");
        List<Media> similarTv = apiService.getSimilarMedia(id,"tv");
        List<Review> tvReviews = apiService.getMediaReviews(id,"tv");
        try{
            User user = myUserDetails.getUser();
            boolean hasWatched = listService.hasWatched(user.getId(),id);
            boolean hasWished = listService.hasWished(user.getId(),id);
            boolean hasPosted = postService.hasPosted(user.getId(), id);
            mav.addObject("hasWatched",hasWatched);
            mav.addObject("hasWished",hasWished);
            mav.addObject("hasPosted",hasPosted);
        }catch (NullPointerException ex){
            System.out.println("user not logged in from /tv/id");
        }
        mav.addObject("tv",tv);

        mav.addObject("runtime", MyUtilities.minToHours(((TV) tv).getEpisodeRuntime().get(0)));
        mav.addObject("releaseYear",tv.getReleaseDate().getYear());
        mav.addObject("customPost",new PostRequestBody());
        mav.addObject("casts",casts);
        mav.addObject("similarTv",similarTv);
        mav.addObject("tvReviews",tvReviews);
        mav.addObject("isReviewsEmpty",tvReviews.isEmpty());
        mav.addObject("numberOfReviews",tvReviews.size());
        return mav;
    }

    @GetMapping("/list/movie/popular")
    public ModelAndView getPopularMovies() throws JsonProcessingException {
        ModelAndView mav = new ModelAndView("list");
        List<Media> movieList = apiService.getPopularMovies();
        mav.addObject("movieList",movieList);
        mav.addObject("topic","Popular Movies");
        return mav;
    }

    @GetMapping("/list/movie/top")
    public ModelAndView getTopRatedMovies() throws JsonProcessingException {
        ModelAndView mav = new ModelAndView("list");
        List<Media> movieList = apiService.getTopRatedMovies();
        mav.addObject("movieList",movieList);
        mav.addObject("topic","Top Rated Movies");
        return mav;
    }

    @GetMapping("/list/movie/upcoming")
    public ModelAndView getUpcomingMovies() throws JsonProcessingException {
        ModelAndView mav = new ModelAndView("list");
        List<Media> movieList = apiService.getUpcomingMovies();
        mav.addObject("movieList",movieList);
        mav.addObject("topic","Upcoming Movies");
        return mav;
    }

    @GetMapping("/list/tv/top")
    public ModelAndView getTopRatedTv() throws JsonProcessingException {
        ModelAndView mav = new ModelAndView("list");
        List<Media> tvList = apiService.getTopRatedTV();
        mav.addObject("tvList",tvList);
        mav.addObject("topic","Top Rated TV Shows");
        return mav;
    }

    @GetMapping("/list/tv/air")
    public ModelAndView getOnAirTv() throws JsonProcessingException {
        ModelAndView mav = new ModelAndView("list");
        List<Media> tvList = apiService.getOnAirTV();
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
        WatchList newMedia = new WatchList(user,media,id,LocalDateTime.now());
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

        WishList newMedia = new WishList(user,media,id,LocalDateTime.now());
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

        Media media = apiService.getMediaByType(
                postRequestBody.getMediaType(),
                Integer.parseInt(postRequestBody.getMediaId())
        );

        System.out.println(media);

        Post post = new Post(
                user,
                postRequestBody.getContent(),
                LocalDateTime.now(),
                postRequestBody.getRating(),
                postRequestBody.getMediaType(),
                Integer.parseInt(postRequestBody.getMediaId())
        );

        postService.save(post);
        String redirectTo = "/" + postRequestBody.getMediaType() + "/" + postRequestBody.getMediaId();
        return new ModelAndView(String.format("redirect:%s", redirectTo));
    }

    @GetMapping("/myposts")
    public ModelAndView myPosts(@AuthenticationPrincipal MyUserDetails myUserDetails,@RequestParam(required = false) Integer pageNo){
        System.out.println("page no is " + pageNo);
        User user = myUserDetails.getUser();
        ModelAndView mav = new ModelAndView("post");
        if (pageNo == null){
            pageNo = 1;
        }
        Page<Post> posts = apiService.getPostsOfUser(user.getId(),pageNo);
        List<DisplayPost> displayPosts = new ArrayList<>();
        for (Post post : posts.getContent()) {
            Media media = apiService.getMediaByType(post.getMediaType(), post.getMediaId());
            DisplayPost displayPost = DisplayPost.builder()
                    .post(post)
                    .media(media)
                    .build();
            displayPosts.add(displayPost);
        }
        mav.addObject("displayPosts", displayPosts);
        mav.addObject("username", user.getUsername());
        mav.addObject("currentPage", pageNo);
        mav.addObject("totalPages", posts.getTotalPages());
        mav.addObject("totalItems", posts.getTotalElements());
        return mav;
    }

    @GetMapping("/login")
    public ModelAndView redirectToHome(){
        return new ModelAndView("redirect:/");
    }

    // TODO: implement post delete feature only for my post and not for other posts



}


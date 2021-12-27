package com.example.rateit.service;

import com.example.rateit.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * created by chethan on 19-12-2021
 **/
@Service
public class APIService {

    private final String url = "https://api.themoviedb.org/3/";
    private final String apiKey = "fc1f6677d898408bfc0966f089fc8088";
    private static final Logger log = LoggerFactory.getLogger(APIService.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private PostService postService;

    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    public Media getMovie(int id){
        String movieUrl = url + "movie/" + id + String.format("?api_key=%s&language=en-US",apiKey);
        try {
            Media movie =  restTemplate.getForObject(movieUrl, Movie.class);
            movie.setMediaType("movie");
            return movie;
        } catch (HttpClientErrorException | NullPointerException err){
            return null;
        }
    }

    public Media getTV(int id){
        String tvUrl = url + "tv/" + id + String.format("?api_key=%s&language=en-US",apiKey);
        try {
            Media tv =  restTemplate.getForObject(tvUrl, TV.class);
            tv.setMediaType("tv");
            return tv;
        }catch (HttpClientErrorException | NullPointerException err){
            return null;
        }
    }

    public List getMovieCastReviewsAndSimilarMovies(int id) throws JsonProcessingException {
        // https://api.themoviedb.org/3/movie/557/credits?api_key=fc1f6677d898408bfc0966f089fc8088&language=en-US
        String movieCastUrl = url + "movie/" + id + String.format("/credits?api_key=%s&language=en-US",apiKey);
        String json = restTemplate.getForObject(movieCastUrl,String.class);
        JsonNode root = objectMapper.readTree(json);
        ArrayNode arrayNode = (ArrayNode) root.get("cast");
        Iterator<JsonNode> node = arrayNode.elements();
        List<Cast> castList = new ArrayList<>();
        while (node.hasNext()){
            JsonNode castNode = node.next();
            Cast cast = objectMapper.treeToValue(castNode,Cast.class);
            castList.add(cast);
        }
        // https://api.themoviedb.org/3/movie/557/similar?api_key=fc1f6677d898408bfc0966f089fc8088&language=en-US&page=1
        String similarMoviesUrl = url + "movie/" + id + String.format("/similar?api_key=%s&language=en-US&page=1",apiKey);
        json = restTemplate.getForObject(similarMoviesUrl,String.class);
        root = objectMapper.readTree(json);
        arrayNode = (ArrayNode) root.get("results");
        Iterator<JsonNode> nodes = arrayNode.elements();
        List<Media> similarMovies = new ArrayList<>();
        while (nodes.hasNext()){
            JsonNode movieNode = nodes.next();
            Media movie = objectMapper.treeToValue(movieNode,Movie.class);
            similarMovies.add(movie);
        }
        // https://api.themoviedb.org/3/movie/557/reviews?api_key=fc1f6677d898408bfc0966f089fc8088&language=en-US&page=1
        String movieReviewUrl = url + "movie/" + id + String.format("/reviews?api_key=%s&language=en-US&page=1",apiKey);
        json = restTemplate.getForObject(movieReviewUrl,String.class);
        root = objectMapper.readTree(json);
        arrayNode = (ArrayNode) root.get("results");
        Iterator<JsonNode> reviewNodes = arrayNode.elements();
        List<Review> movieReviews = new ArrayList<>();
        while (reviewNodes.hasNext()){
            JsonNode reviewNode = reviewNodes.next();
            Review review = objectMapper.treeToValue(reviewNode,Review.class);
            movieReviews.add(review);
        }
        return new ArrayList<>(){
            {
                add(castList);
                add(similarMovies);
                add(movieReviews);
            }
        };
    }

    public List<Media> getTrendingMovies() throws JsonProcessingException {
        String trendingMovieUrl = url + "trending/movie/day" + String.format("?api_key=%s",apiKey);
        Iterator<JsonNode> node = jsonNodeIterator(trendingMovieUrl);
        List<Media> movieList = new ArrayList<>();
        while (node.hasNext()){
            JsonNode movieNode = node.next();
            Media movie = objectMapper.treeToValue(movieNode,Movie.class);
            movieList.add(movie);
        }
        return movieList;
    }

    public List<Media> getPopularMovies() throws JsonProcessingException {
        String popularMovieUrl = url + "/movie/popular" + String.format("?api_key=%s&page=1&language=en-US",apiKey);
        Iterator<JsonNode> node = jsonNodeIterator(popularMovieUrl);
        List<Media> movieList = new ArrayList<>();
        while (node.hasNext()){
            JsonNode movieNode = node.next();
            Movie movie = objectMapper.treeToValue(movieNode,Movie.class);
            movieList.add(movie);
        }
        return movieList;
    }

    public List<Media> getUpcomingMovies() throws JsonProcessingException {
        String upcomingMovieUrl = url + "/movie/upcoming" + String.format("?api_key=%s&page=1&language=en-US",apiKey);
        Iterator<JsonNode> node = jsonNodeIterator(upcomingMovieUrl);
        List<Media> movieList = new ArrayList<>();
        while (node.hasNext()){
            JsonNode movieNode = node.next();
            Media movie = objectMapper.treeToValue(movieNode,Movie.class);
            movieList.add(movie);
        }
        return movieList;
    }

    public List<Media> getTopRatedMovies() throws JsonProcessingException {
        String topRatedMovieUrl = url + "/movie/top_rated" + String.format("?api_key=%s&page=1&language=en-US",apiKey);
        Iterator<JsonNode> node = jsonNodeIterator(topRatedMovieUrl);
        List<Media> movieList = new ArrayList<>();
        while (node.hasNext()){
            JsonNode movieNode = node.next();
            Media movie = objectMapper.treeToValue(movieNode,Movie.class);
            movieList.add(movie);
        }
        return movieList;
    }

    public List<Media> getTrendingTV() throws JsonProcessingException {
        String trendingTVUrl = url + "trending/tv/day" + String.format("?api_key=%s",apiKey);
        Iterator<JsonNode> node = jsonNodeIterator(trendingTVUrl);
        List<Media> tvList = new ArrayList<>();
        while (node.hasNext()){
            JsonNode movieNode = node.next();
            TV tv = objectMapper.treeToValue(movieNode,TV.class);
            tvList.add(tv);
        }
        return tvList;
    }

    public List<Media> getTopRatedTV() throws JsonProcessingException {
        String topRatedTVUrl = url + "/tv/top_rated" + String.format("?api_key=%s&language=en-US&page=1",apiKey);
        Iterator<JsonNode> node = jsonNodeIterator(topRatedTVUrl);
        List<Media> tvList = new ArrayList<>();
        while (node.hasNext()){
            JsonNode movieNode = node.next();
            Media tv = objectMapper.treeToValue(movieNode,TV.class);
            tvList.add(tv);
        }
        return tvList;
    }

    public List<Media> getOnAirTV() throws JsonProcessingException {
        String onAirTVUrl = url + "/tv/on_the_air" + String.format("?api_key=%s&language=en-US&page=1",apiKey);
        Iterator<JsonNode> node = jsonNodeIterator(onAirTVUrl);
        List<Media> tvList = new ArrayList<>();
        while (node.hasNext()){
            JsonNode movieNode = node.next();
            Media tv = objectMapper.treeToValue(movieNode,TV.class);
            tvList.add(tv);
        }
        return tvList;
    }

    private Iterator<JsonNode> jsonNodeIterator(String url) throws JsonProcessingException {
        String json = restTemplate.getForObject(url,String.class);
        JsonNode root = objectMapper.readTree(json);
        ArrayNode arrayNode = (ArrayNode) root.get("results");
        return arrayNode.elements();
    }

    public List<Media> search(String term) throws JsonProcessingException {
        // https://api.themoviedb.org/3/search/multi?api_key=fc1f6677d898408bfc0966f089fc8088&language=en-US&query=far%20from%20home&page=1&include_adult=false
        String searchUrl = url + "search/multi" + String.format("?api_key=%s&language=en-US&query=%s&page=1&include_adult=false",apiKey,term);
        Iterator<JsonNode> node = jsonNodeIterator(searchUrl);
        List<Media> mediaList = new ArrayList<>();
        while (node.hasNext()){
            JsonNode mediaNode = node.next();
            if (mediaNode.get("media_type").asText().equalsIgnoreCase("movie") || mediaNode.get("media_type").asText().equalsIgnoreCase("tv")){
                Media media = objectMapper.treeToValue(mediaNode, Media.class);
                mediaList.add(media);
            }
        }
        return mediaList;
    }

    public List<Media> getPostsOfUser(Long userId){
        List<Post> posts = postService.getUserPosts(userId);
        List<Media> mediaList = new ArrayList<>();
        for (Post post : posts) {
            if (post.getMediaType().equalsIgnoreCase("movie")){
                Media movie = getMovie(post.getMediaId());
                mediaList.add(movie);
            }else if (post.getMediaType().equalsIgnoreCase("tv")){
                Media tv = getTV(post.getMediaId());
                mediaList.add(tv);
            }
        }
        return mediaList;
    }

}

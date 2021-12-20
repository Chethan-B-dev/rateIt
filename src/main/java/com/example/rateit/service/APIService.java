package com.example.rateit.service;

import com.example.rateit.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
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
    private final RestTemplate restTemplate = new RestTemplate();


    public Movie getMovie(int id){
        String movieUrl = url + "movie/" + id + String.format("?api_key=%s&language=en-US",apiKey);
        return restTemplate.getForObject(movieUrl, Movie.class);
    }

    public List getMovieCastReviewsAndSimilarMovies(int id) throws JsonProcessingException {
        // https://api.themoviedb.org/3/movie/557/credits?api_key=fc1f6677d898408bfc0966f089fc8088&language=en-US
        String movieCastUrl = url + "movie/" + id + String.format("/credits?api_key=%s&language=en-US",apiKey);
        String json = restTemplate.getForObject(movieCastUrl,String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
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
        List<Movie> similarMovies = new ArrayList<>();
        while (nodes.hasNext()){
            JsonNode movieNode = nodes.next();
            Movie movie = objectMapper.treeToValue(movieNode,Movie.class);
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

    public TV getTV(int id){
        String movieUrl = url + "tv/" + id + String.format("?api_key=%s&language=en-US",apiKey);
        return restTemplate.getForObject(movieUrl, TV.class);
    }

    public List<Movie> getTrendingMovies() throws JsonProcessingException {
        String trendingMovieUrl = url + "trending/movie/day" + String.format("?api_key=%s",apiKey);
        String json = restTemplate.getForObject(trendingMovieUrl,String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        JsonNode root = objectMapper.readTree(json);
        ArrayNode arrayNode = (ArrayNode) root.get("results");
        Iterator<JsonNode> node = arrayNode.elements();
        List<Movie> movieList = new ArrayList<>();
        while (node.hasNext()){
            JsonNode movieNode = node.next();
            Movie movie = objectMapper.treeToValue(movieNode,Movie.class);
            movieList.add(movie);
        }
        return movieList;
    }

    public List<TV> getTrendingTV() throws JsonProcessingException {
        String trendingTVUrl = url + "trending/tv/day" + String.format("?api_key=%s",apiKey);
        String json = restTemplate.getForObject(trendingTVUrl,String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        JsonNode root = objectMapper.readTree(json);
        ArrayNode arrayNode = (ArrayNode) root.get("results");
        Iterator<JsonNode> node = arrayNode.elements();
        List<TV> tvList = new ArrayList<>();
        while (node.hasNext()){
            JsonNode movieNode = node.next();
            TV tv = objectMapper.treeToValue(movieNode,TV.class);
            tvList.add(tv);
        }
        return tvList;
    }

    public List<Media> search(String term) throws JsonProcessingException {
        // https://api.themoviedb.org/3/search/multi?api_key=fc1f6677d898408bfc0966f089fc8088&language=en-US&query=far%20from%20home&page=1&include_adult=false
        String searchUrl = url + "search/multi" + String.format("?api_key=%s&language=en-US&query=%s&page=1&include_adult=false",apiKey,term);
        String json = restTemplate.getForObject(searchUrl,String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        JsonNode root = objectMapper.readTree(json);
        ArrayNode arrayNode = (ArrayNode) root.get("results");
        Iterator<JsonNode> node = arrayNode.elements();

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

}

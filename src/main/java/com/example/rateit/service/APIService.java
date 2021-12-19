package com.example.rateit.service;

import com.example.rateit.model.Movie;
import com.example.rateit.model.TV;
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

    public List search(String term) throws JsonProcessingException {
        // https://api.themoviedb.org/3/search/multi?api_key=fc1f6677d898408bfc0966f089fc8088&language=en-US&query=far%20from%20home&page=1&include_adult=false
        String searchUrl = url + "search/multi" + String.format("?api_key=%s&language=en-US&query=%s&page=1&include_adult=false",apiKey,term);
        String json = restTemplate.getForObject(searchUrl,String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        JsonNode root = objectMapper.readTree(json);
        ArrayNode arrayNode = (ArrayNode) root.get("results");
        Iterator<JsonNode> node = arrayNode.elements();
        List<Movie> movieList = new ArrayList<>();
        List<TV> tvList = new ArrayList<>();
        while (node.hasNext()){
            JsonNode mediaNode = node.next();
            if (mediaNode.get("media_type").asText().equalsIgnoreCase("movie")){
                Movie movie = objectMapper.treeToValue(mediaNode,Movie.class);
                movieList.add(movie);
            }else if (mediaNode.get("media_type").asText().equalsIgnoreCase("tv")){
                TV tv = objectMapper.treeToValue(mediaNode,TV.class);
                tvList.add(tv);
            }
        }
        System.out.println(movieList);
        System.out.println(tvList);
        return new ArrayList<Integer>();
    }

}

package com.example.rateit.service;

import com.example.rateit.model.*;
import com.example.rateit.model.entity.Post;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    @Autowired
    private MediaCacheService mediaCacheService;

    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    public Media getMovie(int id){
        Media media = mediaCacheService.findById(String.valueOf(id),"movie");
        if (media != null) return media;
        String movieUrl = url + "movie/" + id + String.format("?api_key=%s&language=en-US",apiKey);
        try {
            Media movie =  restTemplate.getForObject(movieUrl, Movie.class);
            System.out.println("made api call in movie");
            movie.setMediaType("movie");
            mediaCacheService.save(movie);
            return movie;
        } catch (HttpClientErrorException | NullPointerException err){
            return null;
        }
    }

    public Media getTV(int id){
        Media media = mediaCacheService.findById(String.valueOf(id),"tv");
        if (media != null) return media;
        String tvUrl = url + "tv/" + id + String.format("?api_key=%s&language=en-US",apiKey);
        try {
            Media tv =  restTemplate.getForObject(tvUrl, TV.class);
            System.out.println("made api call in tv");
            tv.setMediaType("tv");
            mediaCacheService.save(tv);
            return tv;
        }catch (HttpClientErrorException | NullPointerException err){
            return null;
        }
    }

    public List<Review> getMediaReviews(int id,String mediaType) throws JsonProcessingException {
        String movieReviewUrl = url + mediaType + "/" + id + String.format("/reviews?api_key=%s&language=en-US&page=1",apiKey);
        Iterator<JsonNode> reviewNodes = jsonNodeIterator(movieReviewUrl,"results");
        List<Review> movieReviews = new ArrayList<>();
        while (reviewNodes.hasNext()){
            JsonNode reviewNode = reviewNodes.next();
            Review review = objectMapper.treeToValue(reviewNode,Review.class);
            movieReviews.add(review);
        }
        return movieReviews;
    }

    public List<Media> getTrendingMovies() throws JsonProcessingException {
        String trendingMovieUrl = url + "trending/movie/day" + String.format("?api_key=%s",apiKey);
        Iterator<JsonNode> node = jsonNodeIterator(trendingMovieUrl,"results");
        List<Media> movieList = new ArrayList<>();
        while (node.hasNext()){
            JsonNode movieNode = node.next();
            Media movie = objectMapper.treeToValue(movieNode,Movie.class);
            movieList.add(movie);
        }
        return movieList;
    }

    public List<Media> getSimilarMedia(int id,String mediaType) throws JsonProcessingException{
        String similarMoviesUrl = url + mediaType + "/" + id + String.format("/similar?api_key=%s&language=en-US&page=1",apiKey);
        Iterator<JsonNode> nodes = jsonNodeIterator(similarMoviesUrl,"results");
        List<Media> similarMovies = new ArrayList<>();
        while (nodes.hasNext()){
            JsonNode movieNode = nodes.next();
            Media movie = objectMapper.treeToValue(movieNode,Media.class);
            similarMovies.add(movie);
        }
        return similarMovies;
    }

    public List<Cast> getMediaCast(int id,String mediaType) throws JsonProcessingException{
        String movieCastUrl = url + mediaType + "/" + id + String.format("/credits?api_key=%s&language=en-US",apiKey);
        Iterator<JsonNode> node = jsonNodeIterator(movieCastUrl,"cast");
        List<Cast> castList = new ArrayList<>();
        while (node.hasNext()){
            JsonNode castNode = node.next();
            Cast cast = objectMapper.treeToValue(castNode,Cast.class);
            castList.add(cast);
        }
        return castList;
    }

    public List<Media> getPopularMovies() throws JsonProcessingException {
        String popularMovieUrl = url + "/movie/popular" + String.format("?api_key=%s&page=1&language=en-US",apiKey);
        Iterator<JsonNode> node = jsonNodeIterator(popularMovieUrl,"results");
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
        Iterator<JsonNode> node = jsonNodeIterator(upcomingMovieUrl,"results");
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
        Iterator<JsonNode> node = jsonNodeIterator(topRatedMovieUrl,"results");
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
        Iterator<JsonNode> node = jsonNodeIterator(trendingTVUrl,"results");
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
        Iterator<JsonNode> node = jsonNodeIterator(topRatedTVUrl,"results");
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
        Iterator<JsonNode> node = jsonNodeIterator(onAirTVUrl,"results");
        List<Media> tvList = new ArrayList<>();
        while (node.hasNext()){
            JsonNode movieNode = node.next();
            Media tv = objectMapper.treeToValue(movieNode,TV.class);
            tvList.add(tv);
        }
        return tvList;
    }

    private Iterator<JsonNode> jsonNodeIterator(String url,String topic) throws JsonProcessingException {
        String json = restTemplate.getForObject(url,String.class);
        JsonNode root = objectMapper.readTree(json);
        ArrayNode arrayNode = (ArrayNode) root.get(topic);
        return arrayNode.elements();
    }

    public List<Media> search(String term) throws JsonProcessingException {
        // https://api.themoviedb.org/3/search/multi?api_key=fc1f6677d898408bfc0966f089fc8088&language=en-US&query=far%20from%20home&page=1&include_adult=false
        String searchUrl = url + "search/multi" + String.format("?api_key=%s&language=en-US&query=%s&page=1&include_adult=false",apiKey,term);
        Iterator<JsonNode> node = jsonNodeIterator(searchUrl,"results");
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

    public Page<Post> getPostsOfUser(Long userId, int pageNumber){
        return postService.getUserPosts(userId,pageNumber);
    }

    public Media getMediaByType(String mediaType,int mediaId){
        if (mediaType.equalsIgnoreCase("movie"))
            return getMovie(mediaId);
        return getTV(mediaId);
    }

}

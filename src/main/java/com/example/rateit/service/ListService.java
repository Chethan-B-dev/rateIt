package com.example.rateit.service;

import com.example.rateit.model.*;
import com.example.rateit.repository.WatchListRepository;
import com.example.rateit.repository.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * created by chethan on 21-12-2021
 **/
@Service
public class ListService {
    @Autowired
    WatchListRepository watchListRepository;
    @Autowired
    WishListRepository wishListRepository;
    @Autowired
    APIService apiService;

    public WatchList saveWatchList(WatchList watchList){
        return watchListRepository.save(watchList);
    }

    public WishList saveWishList(WishList wishList){
        return wishListRepository.save(wishList);
    }

    public boolean hasWatched(Long userId,int mediaId){
        return watchListRepository.findMediaInWatchList(userId,mediaId).isPresent();
    }

    public boolean hasWished(Long userId,int mediaId){
        return wishListRepository.findMediaInWishList(userId,mediaId).isPresent();
    }


    public List<WatchList> getUserWatchList(Long userId){
        return  watchListRepository.userWatchList(userId);
    }


    public List<WishList> getUserWishList(Long userId){
        return  wishListRepository.userWishList(userId);
    }


    public List<Media> getWatchListMedia(Long userId){
        List<WatchList> watchLists = getUserWatchList(userId);
        List<Media> mediaList = new ArrayList<>();
        for (WatchList watchList: watchLists) {
            if (watchList.getMediaType().equalsIgnoreCase("movie")){
                Movie movie = apiService.getMovie(watchList.getMediaId());
                Media media = new Media(
                        movie.getId(),
                        movie.getTitle(),
                        movie.getTitle(),
                        movie.getOverview(),
                        movie.getLangugae(),
                        movie.getReleaseDate(),
                        movie.getReleaseDate(),
                        movie.getPoster(),
                        "movie"
                );
                mediaList.add(media);
            }else if (watchList.getMediaType().equalsIgnoreCase("tv")){
                TV tv = apiService.getTV(watchList.getMediaId());
                Media media = new Media(
                        tv.getId(),
                        tv.getTitle(),
                        tv.getTitle(),
                        tv.getOverview(),
                        tv.getLangugae(),
                        tv.getReleaseDate(),
                        tv.getReleaseDate(),
                        tv.getPoster(),
                        "tv"
                );
                mediaList.add(media);
            }
        }
        return mediaList;
    }

    public List<Media> getWishListMedia(Long userId){
        List<WishList> wishLists = getUserWishList(userId);
        List<Media> mediaList = new ArrayList<>();
        for (WishList wishList: wishLists) {
            if (wishList.getMediaType().equalsIgnoreCase("movie")){
                Movie movie = apiService.getMovie(wishList.getMediaId());
                Media media = new Media(
                        movie.getId(),
                        movie.getTitle(),
                        movie.getTitle(),
                        movie.getOverview(),
                        movie.getLangugae(),
                        movie.getReleaseDate(),
                        movie.getReleaseDate(),
                        movie.getPoster(),
                        "movie"
                );
                mediaList.add(media);
            }else if (wishList.getMediaType().equalsIgnoreCase("tv")){
                TV tv = apiService.getTV(wishList.getMediaId());
                Media media = new Media(
                        tv.getId(),
                        tv.getTitle(),
                        tv.getTitle(),
                        tv.getOverview(),
                        tv.getLangugae(),
                        tv.getReleaseDate(),
                        tv.getReleaseDate(),
                        tv.getPoster(),
                        "tv"
                );
                mediaList.add(media);
            }
        }
        return mediaList;
    }

}

package com.example.rateit.service;

import com.example.rateit.model.*;
import com.example.rateit.model.entity.WatchList;
import com.example.rateit.model.entity.WishList;
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
                Media movie = apiService.getMovie(watchList.getMediaId());
                mediaList.add(movie);
            }else if (watchList.getMediaType().equalsIgnoreCase("tv")){
                Media tv = apiService.getTV(watchList.getMediaId());
                mediaList.add(tv);
            }
        }
        return mediaList;
    }

    public List<Media> getWishListMedia(Long userId){
        List<WishList> wishLists = getUserWishList(userId);
        List<Media> mediaList = new ArrayList<>();
        for (WishList wishList: wishLists) {
            if (wishList.getMediaType().equalsIgnoreCase("movie")){
                Media movie = apiService.getMovie(wishList.getMediaId());
                mediaList.add(movie);
            }else if (wishList.getMediaType().equalsIgnoreCase("tv")){
                Media tv = apiService.getTV(wishList.getMediaId());
                mediaList.add(tv);
            }
        }
        return mediaList;
    }

    public boolean isMyWatchList(Long userId,int mediaId){
        return watchListRepository.existsByUserIdAndMediaId(userId,mediaId);
    }

    public boolean isMyWishList(Long userId,int mediaId){
        return wishListRepository.existsByUserIdAndMediaId(userId,mediaId);
    }

    public void deleteWatchListByMediaAndUser(Long userId,int mediaId){
        watchListRepository.deleteByMediaIdAndUserId(mediaId,userId);
    }

    public void deleteWishListByMediaAndUser(Long userId,int mediaId){
        wishListRepository.deleteByMediaIdAndUserId(mediaId,userId);
    }

}

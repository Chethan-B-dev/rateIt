package com.example.rateit.service;

import com.example.rateit.model.*;
import com.example.rateit.model.entity.User;
import com.example.rateit.model.entity.WatchList;
import com.example.rateit.model.entity.WishList;
import com.example.rateit.repository.WatchListRepository;
import com.example.rateit.repository.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * created by chethan on 21-12-2021
 **/
@Service
public class ListService {
    @Autowired
    private WatchListRepository watchListRepository;
    @Autowired
    private WishListRepository wishListRepository;
    @Autowired
    private APIService apiService;

    public void saveWatchList(WatchList watchList){
        watchListRepository.save(watchList);
    }

    public void saveWishList(WishList wishList){
        wishListRepository.save(wishList);
    }

    public boolean hasWatched(Long userId, int mediaId){
        return watchListRepository.findMediaInWatchList(userId, mediaId).isPresent();
    }

    public boolean hasWished(Long userId, int mediaId){
        return wishListRepository.findMediaInWishList(userId, mediaId).isPresent();
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

    public boolean isMyWatchList(Long userId, int mediaId){
        return watchListRepository.existsByUserIdAndMediaId(userId, mediaId);
    }

    public boolean isMyWishList(Long userId, int mediaId){
        return wishListRepository.existsByUserIdAndMediaId(userId, mediaId);
    }

    public void deleteWatchListByMediaAndUser(Long userId, int mediaId){
        watchListRepository.deleteByMediaIdAndUserId(mediaId,userId);
    }

    public void deleteWishListByMediaAndUser(Long userId, int mediaId){
        wishListRepository.deleteByMediaIdAndUserId(mediaId,userId);
    }


    @Transactional
    public void deleteMyLists(User currentUser){
        List<WatchList> myWatchlists = watchListRepository.findAllByUser(currentUser);
        List<WishList> myWishLists = wishListRepository.findAllByUser(currentUser);
        watchListRepository.deleteAll(myWatchlists);
        wishListRepository.deleteAll(myWishLists);
    }

}

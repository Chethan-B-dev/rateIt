package com.example.rateit.service;

import com.example.rateit.model.WatchList;
import com.example.rateit.model.WishList;
import com.example.rateit.repository.WatchListRepository;
import com.example.rateit.repository.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * created by chethan on 21-12-2021
 **/
@Service
public class ListService {
    @Autowired
    WatchListRepository watchListRepository;
    @Autowired
    WishListRepository wishListRepository;

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

}

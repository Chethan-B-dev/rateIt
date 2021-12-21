package com.example.rateit.repository;

import com.example.rateit.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WishListRepository extends JpaRepository<WishList,Long> {
    @Query(
            value = "SELECT * from user_wish_list u where u.user_id = :userId and u.media_id = :mediaId",
            nativeQuery = true
    )
    Optional<WishList> findMediaInWishList(Long userId, int mediaId);
}

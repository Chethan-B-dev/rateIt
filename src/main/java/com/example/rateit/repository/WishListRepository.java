package com.example.rateit.repository;

import com.example.rateit.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
@Repository
public interface WishListRepository extends JpaRepository<WishList,Long> {
    @Query(
            value = "SELECT * from user_wish_list u where u.user_id = :userId and u.media_id = :mediaId order by added_at desc",
            nativeQuery = true
    )
    Optional<WishList> findMediaInWishList(Long userId, int mediaId);


    @Query(
            value = "SELECT * from user_wish_list u where u.user_id = :userId order by added_at desc",
            nativeQuery = true
    )
    List<WishList> userWishList(Long userId);

    boolean existsByUserIdAndMediaId(Long userId,int mediaId);

    @Transactional
    @Modifying
    void deleteByMediaIdAndUserId(int mediaId,Long userId);

}

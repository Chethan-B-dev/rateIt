package com.example.rateit.repository;

import com.example.rateit.model.WatchList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * created by chethan on 21-12-2021
 **/
public interface WatchListRepository extends JpaRepository<WatchList,Long> {
    @Query(
            value = "SELECT * from user_watch_list u where u.user_id = :userId and u.media_id = :mediaId",
            nativeQuery = true
    )
    Optional<WatchList> findMediaInWatchList(Long userId, int mediaId);
}

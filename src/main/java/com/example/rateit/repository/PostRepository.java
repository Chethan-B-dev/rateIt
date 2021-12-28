package com.example.rateit.repository;

import com.example.rateit.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * created by chethan on 21-12-2021
 **/
@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    Page<Post> findByUserId(Long userId,Pageable pageable);

    @Query(
            value = "SELECT * from user_posts u where u.user_id = :userId and u.media_id = :mediaId order by u.created_at desc",
            nativeQuery = true
    )
    Optional<Post> findPostByUserAndMedia(Long userId, int mediaId);
}

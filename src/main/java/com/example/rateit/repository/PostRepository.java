package com.example.rateit.repository;

import com.example.rateit.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * created by chethan on 21-12-2021
 **/
@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    Page<Post> findByUserId(Long userId,Pageable pageable);

    @Query(
            value = "SELECT * from user_posts u where u.user_id = :userId and u.media_id = :mediaId",
            nativeQuery = true
    )
    Optional<Post> findPostByUserAndMedia(Long userId, int mediaId);

    @Query(
            value = "SELECT * from user_posts u where u.user_id = :userId and u.id = :postId",
            nativeQuery = true
    )
    Optional<Post> findPostByUserAndPost(Long userId, Long postId);

    Page<Post> findByUserIdInAndUserIdNot(List<Long> userIds,Long myId,Pageable pageable);

}

package com.example.rateit.service;

import com.example.rateit.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * created by chethan on 31-12-2021
 **/

@Service
public class PostCacheService {

    private final String POST_CACHE = "POST";

    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, Post> hashOperations;

    @PostConstruct
    private void intializeHashOperations() {
        hashOperations = redisTemplate.opsForHash();
    }

    public void save(final Post Post) {
        hashOperations.put(POST_CACHE, String.valueOf(Post.getId()), Post);
    }

    public Post findById(final String id) {
        return (Post) hashOperations.get(POST_CACHE, id);
    }

    public Map<String, Post> findAll() {
        return hashOperations.entries(POST_CACHE);
    }

    public void delete(String id) {
        hashOperations.delete(POST_CACHE, id);
    }

}

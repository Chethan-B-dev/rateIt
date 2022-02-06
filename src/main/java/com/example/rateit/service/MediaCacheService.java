package com.example.rateit.service;

import com.example.rateit.model.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * created by chethan on 21-12-2021
 **/
@Service
public class MediaCacheService {

    // this cache helps in showing media details, watchlist , wishlist , posts

    private final String MEDIA_CACHE = "MEDIA";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, Media> hashOperations;

    @PostConstruct
    private void intializeHashOperations() {
        hashOperations = redisTemplate.opsForHash();
    }

    public void save(final Media media) {
        hashOperations.put(MEDIA_CACHE, media.getId() + media.getMediaType(), media);
    }

    public Media findById(final String id,final String mediaType) {
        return (Media) hashOperations.get(MEDIA_CACHE, id + mediaType);
    }

    public Map<String, Media> findAll() {
        return hashOperations.entries(MEDIA_CACHE);
    }

    public void delete(String id, String mediaType) {
        hashOperations.delete(MEDIA_CACHE, id + mediaType);
    }

}

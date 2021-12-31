package com.example.rateit.service;

import com.example.rateit.model.Post;
import com.example.rateit.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * created by chethan on 21-12-2021
 **/
@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;


    public Post save(Post post){
        return postRepository.save(post);
    }

    public Post getPost(Long id){
        return postRepository.getById(id);
    }

    public Page<Post> getUserPosts(Long userId, int pageNum){
        int pageSize = 5;
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        return postRepository.findByUserId(userId,pageable);
    }

    public boolean hasPosted(Long userId,int mediaId){
        return postRepository.findPostByUserAndMedia(userId,mediaId).isPresent();
    }

    public boolean isMyPost(Long userId,Long postId){
        return postRepository.findPostByUserAndPost(userId,postId).isPresent();
    }

    public void deletePost(Long id){
        postRepository.deleteById(id);
    }

    public Page<Post> getFeed(List<Long> userIds, Long myId,int pageNum){
        int pageSize = 5;
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        return postRepository.findByUserIdInAndUserIdNot(userIds, myId, pageable);
    }

}

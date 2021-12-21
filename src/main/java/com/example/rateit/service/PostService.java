package com.example.rateit.service;

import com.example.rateit.model.Post;
import com.example.rateit.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Post> getUserPosts(Long userId){
        return postRepository.findByUserId(userId);
    }

}

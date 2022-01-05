package com.example.rateit.service;

import com.example.rateit.model.entity.User;
import com.example.rateit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * created by chethan on 21-12-2021
 **/
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public User save(User user){
        return userRepository.save(user);
    }

    public User getUser(Long id){
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElse(null);
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public void deleteUser(User currentUser){
        userRepository.delete(currentUser);
    }

}

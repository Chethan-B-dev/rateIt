package com.example.rateit;

import com.example.rateit.repository.FriendRepository;
import com.example.rateit.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * created by chethan on 29-12-2021
 **/
@Component
public class Runner implements CommandLineRunner {

    @Autowired
    FriendService friendService;
    @Autowired
    FriendRepository friendRepository;

    @Override
    public void run(String... args) throws Exception {

    }
}

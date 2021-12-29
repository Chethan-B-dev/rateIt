package com.example.rateit.service;

import com.example.rateit.model.Friend;
import com.example.rateit.model.Status;
import com.example.rateit.model.User;
import com.example.rateit.repository.FriendRepository;
import com.example.rateit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * created by chethan on 29-12-2021
 **/

@Service
public class FriendService {

    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private UserRepository userRepository;

    public Friend saveFriend(User currentUser, Long id) throws NullPointerException{

        Friend friend = new Friend();
        User user1 = currentUser;
        User user2 = userRepository.findById(id).get();
        System.out.println(user1);
        System.out.println(user2);
        User firstuser = user1;
        User seconduser = user2;
        if(user1.getId() > user2.getId()){
            firstuser = user2;
            seconduser = user1;
        }
        if(!(friendRepository.existsByFromAndTo(firstuser,seconduser)) ){
            friend.setCreatedAt(LocalDateTime.now());
            friend.setFrom(firstuser);
            friend.setTo(seconduser);
            friend.setStatus(Status.pending);
            return friendRepository.save(friend);
        }
        return null;
    }

    public List<User> getFriends(User currentUser){

        List<Friend> friendsByFirstUser = friendRepository.findByFrom(currentUser);
        List<Friend> friendsBySecondUser = friendRepository.findByTo(currentUser);
        List<User> friendUsers = new ArrayList<>();

        for (Friend friend : friendsByFirstUser) {
            if (friend.getStatus() == Status.accepted)
                friendUsers.add(userRepository.findById(friend.getTo().getId()).get());
        }

        for (Friend friend : friendsBySecondUser) {
            if (friend.getStatus() == Status.accepted)
                friendUsers.add(userRepository.findById(friend.getFrom().getId()).get());
        }

        return friendUsers;
    }

    public List<User> searchFriends(String query,Long id){
        return userRepository.findByUsernameContainsAndIdNot(query,id);
    }

    public boolean isMyFriend(User from,User to){
        return friendRepository.existsByFromAndTo(from,to) || friendRepository.existsByToAndFrom(from,to);
    }

}

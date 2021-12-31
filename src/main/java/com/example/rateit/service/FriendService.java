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
import java.util.Optional;

/**
 * created by chethan on 29-12-2021
 **/

@Service
public class FriendService {

    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private UserRepository userRepository;

    public Friend saveFriend(User user1, Long id) throws NullPointerException{

        Friend friend = new Friend();
        User user2 = userRepository.findById(id).get();
        User firstuser = user1;
        User seconduser = user2;

        if(user1.getId() > user2.getId()){
            firstuser = user2;
            seconduser = user1;
        }

        if(!(friendRepository.existsByFromAndTo(firstuser,seconduser))){
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

        /*
            suppose there are 3 users with id 1,2,3.
            if user1 add user2 as friend database record will be first user = user1 second user = user2
            if user3 add user2 as friend database record will be first user = user2 second user = user3
            it is because of lexicographical order
            while calling get friends of user 2 we need to check as a both first user and the second user

            first user = user1 second user = user2
            first user = user2 second user = user3

            now i want to get friends of user2

            List<Friend> findbyFirst = [(Friendid = 1,from = user2,to = user3)];
            List<Friend> findbySecond = [(Friendid = 2,from = user1,to = user2)];

            List<User> friends = [];

            for (Friend friend : friendsByFirstUser) {
            if (friend.getStatus() == Status.accepted)
                friends.add(userRepository.findById(friend.getTo().getId()).get());
            }

            friends = [user3];

            for (Friend friend : friendsBySecondUser) {
            if (friend.getStatus() == Status.accepted)
                friendUsers.add(userRepository.findById(friend.getFrom().getId()).get());
            }

            friends = [user3,user1]; // beautiful now this gets all my friends

         */

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
        User firstUser = from;
        User secondUser = to;
        if (from.getId() > to.getId()){
            firstUser = to;
            secondUser = from;
        }
        return isFriendRequestAccepted(firstUser,secondUser);
    }

    public boolean isMyFriend(Long fromId,Long toId){
        User from = userRepository.getById(fromId);
        User to = userRepository.getById(toId);
        User firstUser = from;
        User secondUser = to;
        if (from.getId() > to.getId()){
            firstUser = to;
            secondUser = from;
        }
        return isFriendRequestAccepted(firstUser,secondUser);
    }

    public boolean hasRequested(User from,User to){

        User firstUser = from;
        User secondUser = to;

        if (from.getId() > to.getId()){
            firstUser = to;
            secondUser = from;
        }

        return isFriendRequestPending(firstUser,secondUser);
    }

    public boolean hasRequested(Long fromId,Long toId){

        User from = userRepository.getById(fromId);
        User to = userRepository.getById(toId);
        User firstUser = from;
        User secondUser = to;
        if (from.getId() > to.getId()){
            firstUser = to;
            secondUser = from;
        }

        return isFriendRequestPending(firstUser,secondUser);
    }

    public boolean hasRequested(User from,Long toId){

        User to = userRepository.getById(toId);
        User firstUser = from;
        User secondUser = to;

        if (from.getId() > to.getId()){
            firstUser = to;
            secondUser = from;
        }

        return isFriendRequestPending(firstUser,secondUser);
    }

    private boolean isFriendRequestPending(User firstUser,User secondUser){
        Optional<Friend> friendShip = friendRepository.findByFromAndTo(firstUser,secondUser);

        if (friendShip.isPresent()){
            Friend friend = friendShip.get();
            return friend.getStatus() == Status.pending;
        }

        return false;
    }

    private boolean isFriendRequestAccepted(User firstUser,User secondUser){
        Optional<Friend> friendShip = friendRepository.findByFromAndTo(firstUser,secondUser);

        if (friendShip.isPresent()){
            Friend friend = friendShip.get();
            return friend.getStatus() == Status.accepted;
        }

        return false;
    }

}

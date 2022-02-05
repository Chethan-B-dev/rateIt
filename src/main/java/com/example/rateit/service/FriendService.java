package com.example.rateit.service;

import com.example.rateit.model.entity.Friend;
import com.example.rateit.model.Status;
import com.example.rateit.model.entity.User;
import com.example.rateit.repository.FriendRepository;
import com.example.rateit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    public void saveFriend(User from, Long id) {

        Friend friend = new Friend();
        User to = userRepository.getOne(id);;

        boolean isMyFriend = isMyFriend(from, to);

        if(!isMyFriend){
            friend.setCreatedAt(LocalDateTime.now());
            friend.setFrom(from);
            friend.setTo(to);
            friend.setStatus(Status.pending);
            friendRepository.save(friend);
        }

    }

    public List<User> getFriends(User currentUser){

//        List<Friend> friendsByFirstUser = friendRepository.findByFrom(currentUser);
//        List<Friend> friendsBySecondUser = friendRepository.findByTo(currentUser);
//
        List<User> friendUsers = new ArrayList<>();

        friendRepository.getFriends(currentUser.getId()).forEach(friend -> {
            if (Objects.equals(friend.getFrom().getId(), currentUser.getId()))
                friendUsers.add(friend.getTo());
            else
                friendUsers.add(friend.getFrom());
        });


        /*
            suppose there are 4 users with id 1,2,3,4.
List<Friend> friendsByFirstUser = friendRepository.findByFrom(currentUser);
        List<Friend> friendsBySecondUser = friendRepository.findByTo(currentUser);
        List<User> friendUsers = new ArrayList<>()
            1) if user1 add user2 as friend database record will be first user = user1 second user = user2
            2) if user2 add user3 as friend database record will be first user = user2 second user = user3
            3) if user3 add user1 as friend database record will be first user = user3 second user = user1
            3) if user4 add user2 as friend database record will be first user = user4 second user = user2


            it is because of lexicographical order
            while calling get friends of user 2 we need to check as a both first user and the second user

          1  first user = user1 second user = user2
          2  first user = user2 second user = user3
          3  first user = user4 second user = user2

            now i want to get friends of user2

            List<Friend> findbyFirst = [(Friendid = 2,from = user2,to = user3)];
            List<Friend> findbySecond = [(Friendid = 1,from = user1,to = user2),(Friendid = 3,from = user4,to = user2)];

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

//        friendsByFirstUser.forEach(friend -> {
//            if (friend.getStatus() == Status.accepted)
//                friendUsers.add(friend.getTo());
//        });
//
//        friendsBySecondUser.forEach(friend -> {
//            if (friend.getStatus() == Status.accepted)
//                friendUsers.add(friend.getFrom());
//        });

        return friendUsers;
    }

    public List<User> searchFriends(String query, Long id){
        return userRepository.findByUsernameContainsAndIdNot(query, id);
    }

    public boolean isMyFriend(User from,User to){

        /*
        suppose there are 4 users with id 1,2,3,4.

        1) if user1 add user2 as friend database record will be first user = user1 second user = user2
        2) if user2 add user3 as friend database record will be first user = user2 second user = user3
        3) if user3 add user1 as friend database record will be first user = user3 second user = user1
        3) if user4 add user2 as friend database record will be first user = user4 second user = user2


        it is because of lexicographical order
        while calling get friends of user 2 we need to check as a both first user and the second user

        1  first user = user1 second user = user2
        2  first user = user2 second user = user3
        3  first user = user4 second user = user2

       */


        return friendRepository.isMyFriend(from.getId(),to.getId()).isPresent();
    }

    public boolean isMyFriend(Long fromId,Long toId){
        return friendRepository.isMyFriend(fromId,toId).isPresent();
    }

    public boolean hasRequested(User from,User to){
        return friendRepository.hasRequested(from.getId(),to.getId()).isPresent();
    }

    public List<User> getPendingFriends(User currentUser){

        List<Friend> pendingFriends = friendRepository.getPendingFriends(currentUser.getId());
        List<User> pendingUsers = new ArrayList<>();

        pendingFriends.forEach(friend -> pendingUsers.add(friend.getFrom()));

        return pendingUsers;
    }

    public boolean haveRecived(User me,User friend){
        return friendRepository.haveReceived(me.getId(), friend.getId()).isPresent();
    }

    public void deleteFriend(Long fromId,Long toId){
        friendRepository.deleteByFromIdAndToId(fromId,toId);
    }

    public void acceptFriend(Long fromId,Long toId){
        Optional<Friend> pendingFriend = friendRepository.findByFromIdAndToIdAndStatus(fromId, toId, Status.pending);
        if (pendingFriend.isPresent()){
            Friend friend = pendingFriend.get();
            friend.setStatus(Status.accepted);
            friendRepository.save(friend);
        }
    }

    public boolean hasPendingRequests(Long myId){
        return !friendRepository.getPendingFriends(myId).isEmpty();
    }

    public void unFriend(Long myId,Long friendId){
        Optional<Friend> existingFriend = friendRepository.isMyFriend(myId, friendId);
        existingFriend.ifPresent(friend -> friendRepository.delete(friend));
    }

    public void deleteMyFriendship(User currentUser){
        List<Friend> myFriendships = friendRepository.findByFrom(currentUser);
        myFriendships.addAll(friendRepository.findByTo(currentUser));
        friendRepository.deleteAll(myFriendships);
    }

}

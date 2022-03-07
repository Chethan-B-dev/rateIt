package com.example.rateit.service;

import com.example.rateit.model.Status;
import com.example.rateit.model.entity.Friend;
import com.example.rateit.model.entity.User;
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

    public void saveFriend(User from, Long id) {

        Optional<User> to = userRepository.findById(id);

        if (to.isEmpty())
            return;

        User toUser = to.get();

        if(!isMyFriend(from, toUser)){
            Friend friend = Friend.builder()
                    .to(toUser)
                    .from(from)
                    .status(Status.pending)
                    .createdAt(LocalDateTime.now())
                    .build();
            friendRepository.save(friend);
        }

    }

    public List<User> getFriends(User currentUser){

        List<User> friendUsers = new ArrayList<>();

        friendRepository.getFriends(currentUser.getId()).forEach(friend -> {
            if (friend.getFrom().getId().equals(currentUser.getId()))
                friendUsers.add(friend.getTo());
            else
                friendUsers.add(friend.getFrom());
        });

        return friendUsers;
    }

    public List<User> searchFriends(String query, Long id){
        return userRepository.findByUsernameContainsAndIdNot(query, id);
    }

    public boolean isMyFriend(User from, User to){
        return friendRepository.isMyFriend(from.getId(), to.getId()).isPresent();
    }

    public boolean isMyFriend(Long fromId,Long toId){
        return friendRepository.isMyFriend(fromId, toId).isPresent();
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
        friendRepository.deleteByFromIdAndToId(fromId, toId);
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
        List<Friend> myFriendships = friendRepository.getFriends(currentUser.getId());
        friendRepository.deleteAll(myFriendships);
    }
}

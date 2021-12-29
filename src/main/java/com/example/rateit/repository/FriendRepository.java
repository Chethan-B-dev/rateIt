package com.example.rateit.repository;

import com.example.rateit.model.Friend;
import com.example.rateit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend,Long> {

    boolean existsByFromAndTo(User first, User second);
    boolean existsByToAndFrom(User second,User first);

    boolean existsByTo(User to);

    List<Friend> findByFrom(User user);
    List<Friend> findByTo(User user);

}

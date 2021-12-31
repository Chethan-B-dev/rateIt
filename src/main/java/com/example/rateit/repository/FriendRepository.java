package com.example.rateit.repository;

import com.example.rateit.model.Friend;
import com.example.rateit.model.Status;
import com.example.rateit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend,Long> {


    @Transactional
    @Modifying
    void deleteByFromIdAndToId(Long fromId,Long toId);

    boolean existsByFromAndTo(User first, User second);

    boolean existsByToAndFrom(User first, User second);

    //    boolean existsByToAndFrom(User second,User first);

    boolean existsByTo(User to);

    List<Friend> findByFrom(User user);

    List<Friend> findByTo(User user);

    Optional<Friend> findByFromAndTo(User from, User to);

    Optional<Friend> findByFromIdAndToId(Long fromId,Long toId);

    @Query(
            value = "SELECT * FROM user_friends uf where ( (uf.from_id = :fromId AND uf.to_id = :toId) OR (uf.from_id = :toId AND uf.to_id = :fromId) ) AND uf.status = 1;",
            nativeQuery = true
    )
    Optional<Friend> isMyFriend(Long fromId,Long toId);

    @Query(
            value = "SELECT *" +
                    " FROM user_friends uf" +
                    " where (uf.from_id = :fromId AND uf.to_id = :toId)" +
                    " AND uf.status = 0;",
            nativeQuery = true
    )
    Optional<Friend> hasRequested(Long fromId,Long toId);

    @Query(
            value = "SELECT *" +
                    " FROM user_friends uf" +
                    " where (uf.from_id = :fromId AND uf.to_id = :toId)" +
                    " AND uf.status = 1;",
            nativeQuery = true
    )
    Optional<Friend> hasAccepted(Long fromId,Long toId);

    @Query(
            value = "SELECT *" +
                    " FROM user_friends uf" +
                    " where (uf.from_id = :friendId AND uf.to_id = :myId)" +
                    " AND uf.status = 0;",
            nativeQuery = true
    )
    Optional<Friend> haveReceived(Long myId,Long friendId);

    @Query(
            value = "SELECT *" +
                    " FROM user_friends uf" +
                    " where uf.to_id = :myId" +
                    " AND uf.status = 0;",
            nativeQuery = true
    )
    List<Friend> getPendingFriends(Long myId);

    Optional<Friend> findByFromIdAndToIdAndStatus(Long fromId, Long toId, Status status);

}

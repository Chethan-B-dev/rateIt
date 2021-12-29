package com.example.rateit.repository;

import com.example.rateit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);

    @Query(value = "select * from users u where u.id != :myId",nativeQuery = true)
    List<User> getUsersOtherThanMe(Long myId);

    List<User> findByUsernameContains(String username);

}

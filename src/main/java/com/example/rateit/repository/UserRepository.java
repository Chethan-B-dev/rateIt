package com.example.rateit.repository;

import com.example.rateit.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
    List<User> findByUsernameContainsAndIdNot(String username, Long id);
}

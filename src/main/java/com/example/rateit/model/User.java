package com.example.rateit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * created by chethan on 21-12-2021
 **/

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "username is mandatory")
    private String username;

    @NotBlank(message = "password is mandatory")
    @JsonIgnore
    private String password;

    @Column(nullable = false, unique = true)
    @Email(message = "Email must be valid")
    private String email;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    @JsonIgnore
    public String getFirstLetterOfUsername(){
        return username.substring(0,1);
    }

    @JsonIgnore
    public String getCustomUsername(){
        return username;
    }

}

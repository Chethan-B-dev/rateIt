package com.example.rateit.model;

import lombok.*;

import java.io.Serializable;

/**
 * created by chethan on 19-12-2021
 **/

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Genre implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
}

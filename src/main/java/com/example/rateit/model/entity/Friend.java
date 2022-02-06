package com.example.rateit.model.entity;

import com.example.rateit.model.Status;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * created by chethan on 29-12-2021
 **/


@Entity
@Table(name = "user_friends")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "from_id", referencedColumnName = "id")
    private User from;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "to_id", referencedColumnName = "id")
    private User to;

    private Status status;

    public Friend(LocalDateTime createdAt, User from, User to, Status status) {
        this.createdAt = createdAt;
        this.from = from;
        this.to = to;
        this.status = status;
    }
}

// UserMembership.java
package com.sparta.fltpleprojectbackend.usermembership.entity;

import com.sparta.fltpleprojectbackend.store.entity.Store;
import com.sparta.fltpleprojectbackend.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class UserMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    private String membershipType;
    private String membershipStatus;

    public UserMembership(User user, String membershipType, String membershipStatus) {
        this.user = user;
        this.membershipType = membershipType;
        this.membershipStatus = membershipStatus;
    }

    public void updateMembership(String membershipType, String membershipStatus) {
        this.membershipType = membershipType;
        this.membershipStatus = membershipStatus;
    }
}
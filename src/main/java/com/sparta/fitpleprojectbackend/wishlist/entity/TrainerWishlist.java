package com.sparta.fitpleprojectbackend.wishlist.entity;

import com.sparta.fitpleprojectbackend.trainer.entity.Trainer;
import com.sparta.fitpleprojectbackend.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class TrainerWishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;

    @Column(nullable = false)
    private boolean isWish;

    public TrainerWishlist(User user, Trainer trainer) {
        this.user = user;
        this.trainer = trainer;
        this.isWish = true;
    }

    public void update() {
        this.isWish = !this.isWish;
    }
}

package com.sparta.fitpleprojectbackend.userpt.entity;

import com.sparta.fitpleprojectbackend.common.TimeStamped;
import com.sparta.fitpleprojectbackend.store.entity.Store;
import com.sparta.fitpleprojectbackend.user.entity.User;
import com.sparta.fitpleprojectbackend.trainer.entity.Trainer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class UserPt extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    private Long ptPaymentId; // PT Payment ID (not yet implemented)

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private int ptCount;

    @Column(nullable = false)
    private double ptPrice;

    public UserPt(User user, Long ptPaymentId, Trainer trainer, int ptCount, double ptPrice) {
        this.user = user;
        this.ptPaymentId = ptPaymentId;
        this.trainer = trainer;
        this.ptCount = ptCount;
        this.ptPrice = ptPrice;
    }
}
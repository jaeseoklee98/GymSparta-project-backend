package com.sparta.fitpleprojectbackend.ptsesson.entity;

import com.sparta.fitpleprojectbackend.common.TimeStamped;
import com.sparta.fitpleprojectbackend.ptsesson.enums.PaymentStatus;
import com.sparta.fitpleprojectbackend.ptsesson.enums.PaymentType;
import com.sparta.fitpleprojectbackend.ptsesson.enums.PtTimes;
import com.sparta.fitpleprojectbackend.trainer.entity.Trainer;
import com.sparta.fitpleprojectbackend.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
@Entity
public class PtPayment extends TimeStamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "trainer_id", nullable = false)
  private Trainer trainer;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PtTimes ptTimes;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PaymentType paymentType;

  @Column
  private double amount;

  @Enumerated(EnumType.STRING)
  private PaymentStatus paymentStatus;

  @Column
  private LocalDateTime paymentDate;

  @Column
  private LocalDateTime expiryDate;

  @Column
  private boolean isMembership;

  protected PtPayment() {
  }

  public PtPayment(Trainer trainer, User user, PtTimes ptTimes, PaymentType paymentType,
      double amount, PaymentStatus paymentStatus, LocalDateTime paymentDate,
      LocalDateTime expiryDate, boolean isMembership) {
    this.trainer = trainer;
    this.user = user;
    this.ptTimes = ptTimes;
    this.paymentType = paymentType;
    this.amount = amount;
    this.paymentStatus = paymentStatus;
    this.paymentDate = paymentDate;
    this.expiryDate = expiryDate;
    this.isMembership = isMembership;
  }
}

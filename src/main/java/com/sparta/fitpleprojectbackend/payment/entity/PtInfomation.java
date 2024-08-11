package com.sparta.fitpleprojectbackend.payment.entity;

import com.sparta.fitpleprojectbackend.common.TimeStamped;
import com.sparta.fitpleprojectbackend.payment.enums.PtTimes;
import com.sparta.fitpleprojectbackend.payment.enums.PaymentStatus;
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
import lombok.Getter;

@Getter
@Entity
public class PtInfomation extends TimeStamped {

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
  private PtTimes ptTimes = PtTimes.TEN_TIMES;

  @Column(nullable = false)
  private double ptPrice;

  @Column
  private boolean isMembership;

  @Enumerated(EnumType.STRING)
  private PaymentStatus paymentStatus;

  protected PtInfomation() {
  }

  public PtInfomation(Trainer trainer, User user, double ptPrice, boolean isMembership, PtTimes ptTimes, PaymentStatus paymentStatus) {
    this.trainer = trainer;
    this.user = user;
    this.ptPrice = ptPrice;
    this.isMembership = isMembership;
    this.ptTimes = ptTimes;
    this.paymentStatus = paymentStatus;
  }
}
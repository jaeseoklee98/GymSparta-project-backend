package com.sparta.fitpleprojectbackend.payment.entity;

import com.sparta.fitpleprojectbackend.common.TimeStamped;
import com.sparta.fitpleprojectbackend.payment.enums.PaymentStatus;
import com.sparta.fitpleprojectbackend.payment.enums.PaymentType;
import com.sparta.fitpleprojectbackend.payment.enums.PtTimes;
import com.sparta.fitpleprojectbackend.trainer.entity.Trainer;
import com.sparta.fitpleprojectbackend.user.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class Payment extends TimeStamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long paymentId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "trainer_id")
  private Trainer trainer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  private Product product;

  @Enumerated(EnumType.STRING)
  @Column
  private PtTimes ptTimes;

  @Setter
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PaymentType paymentType;

  @Setter
  @Column(nullable = false)
  private double amount;

  @Setter
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PaymentStatus paymentStatus;

  @Column(nullable = false)
  private LocalDateTime paymentDate;

  @Column
  private LocalDateTime expiryDate;

  @Column
  private boolean isMembership;

  public Payment() {
  }

  // 새로운 생성자: 장바구니 결제용
  public Payment(User user, double amount) {
    this.user = user;
    this.amount = amount;
    this.paymentStatus = PaymentStatus.PENDING; // 기본값 설정
    this.paymentDate = LocalDateTime.now(); // 기본값 설정
  }

  // 기존 생성자: PT 세션, 회원권 결제용
  public Payment(Trainer trainer, User user, Product product, PtTimes ptTimes, PaymentType paymentType, double amount, PaymentStatus paymentStatus, LocalDateTime paymentDate, LocalDateTime expiryDate, boolean isMembership) {
    this.trainer = trainer;
    this.user = user;
    this.product = product;
    this.ptTimes = ptTimes;
    this.paymentType = paymentType;
    this.amount = amount;
    this.paymentStatus = paymentStatus;
    this.paymentDate = paymentDate;
    this.expiryDate = expiryDate;
    this.isMembership = isMembership;
  }
}
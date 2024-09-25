package com.sparta.gymspartaprojectbackend.product.entity;

import com.sparta.gymspartaprojectbackend.common.TimeStamped;
import com.sparta.gymspartaprojectbackend.payment.enums.PaymentStatus;
import com.sparta.gymspartaprojectbackend.payment.enums.PaymentType;
import com.sparta.gymspartaprojectbackend.payment.enums.PtTimes;
import com.sparta.gymspartaprojectbackend.trainer.entity.Trainer;
import com.sparta.gymspartaprojectbackend.user.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
@Entity
public class Product extends TimeStamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "trainer_id", nullable = true)
  private Trainer trainer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Enumerated(EnumType.STRING)
  @Column(nullable = true)
  private PtTimes ptTimes;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PaymentType paymentType;

  @Column(nullable = false)
  private double amount;

  @Getter
  @Column(nullable = false)
  private double productPrice;

  @Enumerated(EnumType.STRING)
  private PaymentStatus paymentStatus;

  @Column
  private LocalDateTime paymentDate;

  @Column
  private LocalDateTime expiryDate;

  @Column(nullable = false)
  private boolean isActive;

  protected Product() {
  }

  public Product(Trainer trainer, User user, PtTimes ptTimes, PaymentType paymentType,
    double amount, double productPrice, PaymentStatus paymentStatus, LocalDateTime paymentDate,
    LocalDateTime expiryDate, boolean isActive) {
    this.trainer = trainer;
    this.user = user;
    this.ptTimes = ptTimes;
    this.paymentType = paymentType;
    this.amount = amount;
    this.productPrice = productPrice;
    this.paymentStatus = paymentStatus;
    this.paymentDate = paymentDate;
    this.expiryDate = expiryDate;
    this.isActive = isActive;
  }

  public Product(User user, PaymentType paymentType, double amount, double productPrice,
    PaymentStatus paymentStatus, LocalDateTime paymentDate, LocalDateTime expiryDate,
    boolean isActive) {
    this.user = user;
    this.paymentType = paymentType;
    this.amount = amount;
    this.productPrice = productPrice;
    this.paymentStatus = paymentStatus;
    this.paymentDate = paymentDate;
    this.expiryDate = expiryDate;
    this.isActive = isActive;
  }
}
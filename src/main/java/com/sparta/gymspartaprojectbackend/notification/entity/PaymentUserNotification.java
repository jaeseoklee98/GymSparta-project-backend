package com.sparta.gymspartaprojectbackend.notification.entity;

import com.sparta.gymspartaprojectbackend.common.TimeStamped;
import com.sparta.gymspartaprojectbackend.payment.entity.Payment;
import com.sparta.gymspartaprojectbackend.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class PaymentUserNotification extends TimeStamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  private String message;

  @OneToOne
  @JoinColumn(name = "payment_id")
  private Payment payment;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  public PaymentUserNotification(String title, String message, Payment payment) {
    this.title = title;
    this.message = message;
    this.payment = payment;
    this.user = payment.getUser();
  }
}
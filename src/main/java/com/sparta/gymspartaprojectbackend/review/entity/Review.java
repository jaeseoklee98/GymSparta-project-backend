package com.sparta.gymspartaprojectbackend.review.entity;

import com.sparta.gymspartaprojectbackend.common.TimeStamped;
import com.sparta.gymspartaprojectbackend.review.enums.ReviewType;
import com.sparta.gymspartaprojectbackend.store.entity.Store;
import com.sparta.gymspartaprojectbackend.trainer.entity.Trainer;
import com.sparta.gymspartaprojectbackend.user.entity.User;
import com.sparta.gymspartaprojectbackend.payment.entity.Payment;
import com.sparta.gymspartaprojectbackend.product.entity.Product;
import jakarta.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Review extends TimeStamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "store_id", nullable = true)
  private Store store;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "trainer_id", nullable = true)
  private Trainer trainer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "payment_id", nullable = false)
  private Payment payment;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = true)
  private Product product;

  @Column(nullable = false)
  private int rating;

  @Column(nullable = false)
  private String comment;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ReviewType reviewType; // STORE or TRAINER

  @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ReviewImage> reviewImages;

  public Review(User user, Store store, Trainer trainer, Payment payment, Product product, int rating, String comment, ReviewType reviewType) {
    this.user = user;
    this.store = store;
    this.trainer = trainer;
    this.payment = payment;
    this.product = product;
    this.rating = rating;
    this.comment = comment;
    this.reviewType = reviewType;
  }

  public void update(int rating, String comment) {
    this.rating = rating;
    this.comment = comment;
  }
}
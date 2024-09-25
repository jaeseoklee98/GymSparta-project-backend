package com.sparta.gymspartaprojectbackend.review.entity;

import com.sparta.gymspartaprojectbackend.review.enums.ImageType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ReviewImage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "review_id", nullable = false)
  private Review review;

  @Column(nullable = false)
  private String imageUrl;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ImageType imageType; // STORE or TRAINER

  public ReviewImage(Review review, String imageUrl, ImageType imageType) {
    this.review = review;
    this.imageUrl = imageUrl;
    this.imageType = imageType;
  }
}
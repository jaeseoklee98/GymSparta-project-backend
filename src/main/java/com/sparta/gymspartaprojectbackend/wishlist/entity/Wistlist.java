package com.sparta.gymspartaprojectbackend.wishlist.entity;

import com.sparta.gymspartaprojectbackend.user.entity.User;
import com.sparta.gymspartaprojectbackend.wishlist.enums.WishType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Wistlist {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(nullable = false)
  private WishType wishType;

  @Column
  private boolean isWish;

  public Wistlist(User user, WishType wishType, boolean isWish) {
    this.user = user;
    this.wishType = wishType;
    this.isWish = isWish;
  }

  public void toggleWish() {
    this.isWish = !this.isWish;
  }
}
package com.sparta.fitpleprojectbackend.payment.entity;

import com.sparta.fitpleprojectbackend.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Cart {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long cartId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "cart_id")
  private List<CartItem> cartItems = new ArrayList<>();

  public Cart(User user) {
    this.user = user;
  }

  public void addItem(CartItem cartItem) {
    this.cartItems.add(cartItem);
  }

  public void removeItem(CartItem cartItem) {
    this.cartItems.remove(cartItem);
  }
}
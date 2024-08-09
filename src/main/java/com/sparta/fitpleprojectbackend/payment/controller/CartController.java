package com.sparta.fitpleprojectbackend.payment.controller;

import com.sparta.fitpleprojectbackend.payment.entity.Cart;
import com.sparta.fitpleprojectbackend.payment.entity.CartItem;
import com.sparta.fitpleprojectbackend.payment.entity.Payment;
import com.sparta.fitpleprojectbackend.payment.service.CartService;
import com.sparta.fitpleprojectbackend.security.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

  private final CartService cartService;

  public CartController(CartService cartService) {
    this.cartService = cartService;
  }

  @PostMapping("/add")
  public ResponseEntity<Cart> addToCart(@RequestParam Long productId, @RequestParam int quantity) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    Cart cart = cartService.addToCart(userDetails.getUserId(), productId, quantity);
    return ResponseEntity.ok(cart);
  }

  @DeleteMapping("/remove/{cartItemId}")
  public ResponseEntity<Void> removeFromCart(@PathVariable Long cartItemId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    cartService.removeFromCart(userDetails.getUserId(), cartItemId);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/items")
  public ResponseEntity<List<CartItem>> getCartItems() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<CartItem> cartItems = cartService.getCartItems(userDetails.getUserId());
    return ResponseEntity.ok(cartItems);
  }

  @PostMapping("/checkout")
  public ResponseEntity<Payment> checkout() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    Payment payment = cartService.checkout(userDetails.getUserId());
    return ResponseEntity.ok(payment);
  }
}
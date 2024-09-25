package com.sparta.gymspartaprojectbackend.cart.controller;

import com.sparta.gymspartaprojectbackend.cart.entity.Cart;
import com.sparta.gymspartaprojectbackend.cart.entity.CartItem;
import com.sparta.gymspartaprojectbackend.payment.entity.Payment;
import com.sparta.gymspartaprojectbackend.cart.service.CartService;
import com.sparta.gymspartaprojectbackend.security.UserDetailsImpl;
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
  public ResponseEntity<Payment> checkout(@RequestParam Long storeId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    Payment payment = cartService.checkout(userDetails.getUserId(), storeId);
    return ResponseEntity.ok(payment);
  }
}
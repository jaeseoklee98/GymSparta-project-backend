package com.sparta.fitpleprojectbackend.payment.service;

import com.sparta.fitpleprojectbackend.enums.ErrorType;
import com.sparta.fitpleprojectbackend.exception.CustomException;
import com.sparta.fitpleprojectbackend.payment.entity.Cart;
import com.sparta.fitpleprojectbackend.payment.entity.CartItem;
import com.sparta.fitpleprojectbackend.payment.entity.Payment;
import com.sparta.fitpleprojectbackend.payment.repository.CartRepository;
import com.sparta.fitpleprojectbackend.payment.repository.PaymentRepository.PaymentRepository;
import com.sparta.fitpleprojectbackend.payment.entity.Product;
import com.sparta.fitpleprojectbackend.payment.repository.ProductRepository;
import com.sparta.fitpleprojectbackend.user.entity.User;
import com.sparta.fitpleprojectbackend.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartService {

  private final CartRepository cartRepository;
  private final ProductRepository productRepository;
  private final PaymentRepository paymentRepository;
  private final UserRepository userRepository;

  public CartService(CartRepository cartRepository, ProductRepository productRepository,
      PaymentRepository paymentRepository, UserRepository userRepository) {
    this.cartRepository = cartRepository;
    this.productRepository = productRepository;
    this.paymentRepository = paymentRepository;
    this.userRepository = userRepository;
  }

  @Transactional
  public Cart addToCart(Long userId, Long productId, int quantity) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));

    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new CustomException(ErrorType.PRODUCT_NOT_FOUND));

    Cart cart = cartRepository.findByUserUserId(userId)
        .orElse(new Cart(user));

    CartItem cartItem = new CartItem(product, quantity);
    cart.addItem(cartItem);

    return cartRepository.save(cart);
  }

  @Transactional
  public void removeFromCart(Long userId, Long cartItemId) {
    Cart cart = cartRepository.findByUserUserId(userId)
        .orElseThrow(() -> new CustomException(ErrorType.CART_NOT_FOUND));

    CartItem cartItem = cart.getCartItems().stream()
        .filter(item -> item.getCartItemId().equals(cartItemId))
        .findFirst()
        .orElseThrow(() -> new CustomException(ErrorType.CART_ITEM_NOT_FOUND));

    cart.removeItem(cartItem);
    cartRepository.save(cart);
  }

  @Transactional
  public Payment checkout(Long userId) {
    Cart cart = cartRepository.findByUserUserId(userId)
        .orElseThrow(() -> new CustomException(ErrorType.CART_NOT_FOUND));

    if (cart.getCartItems().isEmpty()) {
      throw new CustomException(ErrorType.CART_EMPTY);
    }

    // 여기서는 간단히 합계 계산 후 Payment 엔티티를 생성
    double totalAmount = cart.getCartItems().stream()
        .mapToDouble(item -> item.getProduct().getProductPrice() * item.getQuantity())
        .sum();

    // 수정된 생성자 사용
    Payment payment = new Payment(cart.getUser(), totalAmount);
    paymentRepository.save(payment);

    // 결제 후 장바구니 비우기
    cart.getCartItems().clear();
    cartRepository.save(cart);

    return payment;
  }

  public List<CartItem> getCartItems(Long userId) {
    Cart cart = cartRepository.findByUserUserId(userId)
        .orElseThrow(() -> new CustomException(ErrorType.CART_NOT_FOUND));
    return cart.getCartItems();
  }
}
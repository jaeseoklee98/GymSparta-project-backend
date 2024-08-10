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
import com.sparta.fitpleprojectbackend.store.entity.Store;
import com.sparta.fitpleprojectbackend.store.repository.StoreRepository;
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
  private final StoreRepository storeRepository;

  public CartService(CartRepository cartRepository, ProductRepository productRepository,
      PaymentRepository paymentRepository, UserRepository userRepository, StoreRepository storeRepository) {
    this.cartRepository = cartRepository;
    this.productRepository = productRepository;
    this.paymentRepository = paymentRepository;
    this.userRepository = userRepository;
    this.storeRepository = storeRepository;
  }

  @Transactional
  public Cart addToCart(Long userId, Long productId, int quantity) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));

    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new CustomException(ErrorType.PRODUCT_NOT_FOUND));

    Cart cart = cartRepository.findByUser_Id(userId) // 메서드 이름을 업데이트된 대로 사용
        .orElse(new Cart(user));

    CartItem cartItem = new CartItem(product, quantity);
    cart.addItem(cartItem);

    return cartRepository.save(cart);
  }

  @Transactional
  public void removeFromCart(Long userId, Long cartItemId) {
    Cart cart = cartRepository.findByUser_Id(userId) // 메서드 이름을 업데이트된 대로 사용
        .orElseThrow(() -> new CustomException(ErrorType.CART_NOT_FOUND));

    CartItem cartItem = cart.getCartItems().stream()
        .filter(item -> item.getCartItemId().equals(cartItemId))
        .findFirst()
        .orElseThrow(() -> new CustomException(ErrorType.CART_ITEM_NOT_FOUND));

    cart.removeItem(cartItem);
    cartRepository.save(cart);
  }

  @Transactional
  public Payment checkout(Long userId, Long storeId) {
    Cart cart = cartRepository.findByUser_Id(userId) // 메서드 이름을 업데이트된 대로 사용
        .orElseThrow(() -> new CustomException(ErrorType.CART_NOT_FOUND));

    if (cart.getCartItems().isEmpty()) {
      throw new CustomException(ErrorType.CART_EMPTY);
    }

    // Store 정보를 가져오기
    Store store = storeRepository.findById(storeId)
        .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_STORE));

    double totalAmount = cart.getCartItems().stream()
        .mapToDouble(item -> item.getProduct().getProductPrice() * item.getQuantity())
        .sum();

    // store와 함께 Payment 생성
    Payment payment = new Payment(cart.getUser(), store, totalAmount);
    paymentRepository.save(payment);

    // 결제 후 장바구니 비우기
    cart.getCartItems().clear();
    cartRepository.save(cart);

    return payment;
  }

  public List<CartItem> getCartItems(Long userId) {
    Cart cart = cartRepository.findByUser_Id(userId) // 메서드 이름을 업데이트된 대로 사용
        .orElseThrow(() -> new CustomException(ErrorType.CART_NOT_FOUND));
    return cart.getCartItems();
  }
}
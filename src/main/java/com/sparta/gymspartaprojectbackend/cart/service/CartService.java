package com.sparta.gymspartaprojectbackend.cart.service;

import com.sparta.gymspartaprojectbackend.enums.ErrorType;
import com.sparta.gymspartaprojectbackend.exception.CustomException;
import com.sparta.gymspartaprojectbackend.cart.entity.Cart;
import com.sparta.gymspartaprojectbackend.cart.entity.CartItem;
import com.sparta.gymspartaprojectbackend.payment.entity.Payment;
import com.sparta.gymspartaprojectbackend.cart.repository.CartRepository;
import com.sparta.gymspartaprojectbackend.product.entity.Product;
import com.sparta.gymspartaprojectbackend.product.repository.ProductRepository;
import com.sparta.gymspartaprojectbackend.payment.repository.PaymentRepository;
import com.sparta.gymspartaprojectbackend.store.entity.Store;
import com.sparta.gymspartaprojectbackend.store.repository.StoreRepository;
import com.sparta.gymspartaprojectbackend.user.entity.User;
import com.sparta.gymspartaprojectbackend.user.repository.UserRepository;
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

    Cart cart = getOrCreateCart(userId);

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
    Cart cart = getOrCreateCart(userId);

    if (cart.getCartItems().isEmpty()) {
      throw new CustomException(ErrorType.CART_EMPTY);
    }

    Store store = storeRepository.findById(storeId)
        .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_STORE));

    double totalAmount = cart.getCartItems().stream()
        .mapToDouble(item -> item.getProduct().getProductPrice() * item.getQuantity())
        .sum();

    Payment payment = new Payment(cart.getUser(), store, totalAmount);
    paymentRepository.save(payment);

    cart.getCartItems().clear();
    cartRepository.save(cart);

    return payment;
  }

  public List<CartItem> getCartItems(Long userId) {
    Cart cart = getOrCreateCart(userId);
    if (cart.getCartItems().isEmpty()) {
      throw new CustomException(ErrorType.CART_EMPTY);
    }
    return cart.getCartItems();
  }

  public Cart getOrCreateCart(Long userId) {
    return cartRepository.findByUser_Id(userId)
        .orElseGet(() -> {
          User user = userRepository.findById(userId)
              .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));
          Cart newCart = new Cart(user);
          return cartRepository.save(newCart);
        });
  }
}
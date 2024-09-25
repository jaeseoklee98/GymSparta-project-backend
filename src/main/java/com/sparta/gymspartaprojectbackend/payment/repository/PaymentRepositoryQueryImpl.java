package com.sparta.gymspartaprojectbackend.payment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.gymspartaprojectbackend.payment.entity.QPayment;
import com.sparta.gymspartaprojectbackend.payment.enums.ProductType;
import com.sparta.gymspartaprojectbackend.user.entity.QUser;
import com.sparta.gymspartaprojectbackend.user.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryQueryImpl implements PaymentRepositoryQuery {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<User> findUserByStoreId(Long storeId) {
    QPayment payment = QPayment.payment;
    QUser user = QUser.user;

    return queryFactory
      .select(user)
      .distinct()
      .from(payment)
      .join(payment.user, user)
      .where(payment.store.id.eq(storeId))
      .fetch();
  }

  @Override
  public List<User> findPtExpiringSoon(LocalDateTime today, LocalDateTime twoDaysLater) {
    QPayment payment = QPayment.payment;

    return queryFactory
      .select(payment.user)
      .from(payment)
      .where(payment.productType.eq(ProductType.PT_SESSION)
        .and(payment.expiryDate.between(today, twoDaysLater)))
      .distinct() // 중복된 유저 제거
      .fetch();
  }

  @Override
  public List<User> findMembershipExpiringSoon(LocalDateTime today, LocalDateTime twoDaysLater) {
    QPayment payment = QPayment.payment;

    return queryFactory
      .select(payment.user)
      .from(payment)
      .where(payment.productType.eq(ProductType.MEMBERSHIP)
        .and(payment.expiryDate.between(today, twoDaysLater)))
      .distinct() // 중복된 유저 제거
      .fetch();
  }
}

package com.sparta.fitpleprojectbackend.payment.repository.PaymentRepository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.fitpleprojectbackend.payment.entity.QPayment;
import com.sparta.fitpleprojectbackend.payment.entity.QProduct;
import com.sparta.fitpleprojectbackend.user.entity.User;
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
    QProduct product = QProduct.product;

    return queryFactory
      .select(payment.user)
      .from(payment)
      .join(payment.product, product)
      .where(product.store.id.eq(storeId))
      .fetch();
  }
}

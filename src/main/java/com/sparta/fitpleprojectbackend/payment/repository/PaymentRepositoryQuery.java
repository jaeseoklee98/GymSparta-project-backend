package com.sparta.fitpleprojectbackend.payment.repository;

import com.sparta.fitpleprojectbackend.user.entity.User;
import java.util.List;

public interface PaymentRepositoryQuery {
  List<User> findUserByStoreId(Long storeId);
}

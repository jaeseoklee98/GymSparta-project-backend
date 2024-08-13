package com.sparta.gymspartaprojectbackend.payment.repository;

import com.sparta.gymspartaprojectbackend.user.entity.User;
import java.util.List;

public interface PaymentRepositoryQuery {
  List<User> findUserByStoreId(Long storeId);
}

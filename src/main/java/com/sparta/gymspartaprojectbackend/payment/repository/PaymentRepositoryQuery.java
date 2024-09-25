package com.sparta.gymspartaprojectbackend.payment.repository;

import com.sparta.gymspartaprojectbackend.user.entity.User;
import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepositoryQuery {
  List<User> findUserByStoreId(Long storeId);
  List<User> findPtExpiringSoon(LocalDateTime today, LocalDateTime twoDaysLater);
  List<User> findMembershipExpiringSoon(LocalDateTime today, LocalDateTime twoDaysLater);
}

package com.sparta.fitpleprojectbackend.userpt.repository;
import com.sparta.fitpleprojectbackend.payment.entity.UserPt;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface UserPtRepositoryQuery {
  List<UserPt> findPtExpiringSoon(LocalDateTime today, LocalDateTime twoDaysLater);
}

package com.sparta.fitpleprojectbackend.userpt.repository;
import com.sparta.fitpleprojectbackend.userpt.entity.UserPt;
import java.time.LocalDateTime;
import java.util.List;

public interface UserPtRepositoryQuery {
  List<UserPt> findPtExpiringSoon(LocalDateTime today, LocalDateTime twoDaysLater);
}

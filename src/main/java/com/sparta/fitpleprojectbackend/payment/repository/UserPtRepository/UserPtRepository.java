package com.sparta.fitpleprojectbackend.payment.repository.UserPtRepository;

import com.sparta.fitpleprojectbackend.payment.entity.UserPt;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPtRepository extends JpaRepository<UserPt, Long>, UserPtRepositoryQuery {

  List<UserPt> findAllByTrainerIdAndUserIdAndIsActive(
      Long trainerId, Long userId, boolean isActive);
}
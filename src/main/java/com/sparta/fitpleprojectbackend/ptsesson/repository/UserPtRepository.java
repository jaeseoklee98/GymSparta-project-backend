package com.sparta.fitpleprojectbackend.ptsesson.repository;

import com.sparta.fitpleprojectbackend.ptsesson.entity.UserPt;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPtRepository extends JpaRepository<UserPt, Long> {

  List<UserPt> findAllByTrainerIdAndUserIdAndIsActive(
      Long trainerId, Long userId, boolean isActive);
}
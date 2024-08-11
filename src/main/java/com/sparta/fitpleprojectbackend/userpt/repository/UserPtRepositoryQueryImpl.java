package com.sparta.fitpleprojectbackend.userpt.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.fitpleprojectbackend.userpt.entity.QUserPt;
import com.sparta.fitpleprojectbackend.userpt.entity.UserPt;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserPtRepositoryQueryImpl implements UserPtRepositoryQuery {

  private JPAQueryFactory queryFactory;


  @Override
  public List<UserPt> findPtExpiringSoon(LocalDateTime today, LocalDateTime twoDaysLater) {
    QUserPt userPt = QUserPt.userPt;

    return queryFactory.selectFrom(userPt)
      .where(userPt.expiryDate.between(today, twoDaysLater))
      .fetch();
  }
}
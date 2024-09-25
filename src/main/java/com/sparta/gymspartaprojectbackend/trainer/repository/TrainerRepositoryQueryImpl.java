package com.sparta.gymspartaprojectbackend.trainer.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.gymspartaprojectbackend.owner.entity.QOwner;
import com.sparta.gymspartaprojectbackend.store.entity.QStore;
import com.sparta.gymspartaprojectbackend.trainer.entity.QTrainer;
import com.sparta.gymspartaprojectbackend.trainer.entity.Trainer;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TrainerRepositoryQueryImpl implements TrainerRepositoryQuery {

  private final JPAQueryFactory queryFactory;


  @Override
  public List<Trainer> findAllTrainersByOwnerId(Long ownerId) {

    QTrainer trainer = QTrainer.trainer;
    QStore store = QStore.store;
    QOwner owner = QOwner.owner;

    return queryFactory.selectFrom(trainer)
      .join(trainer.store, store).fetchJoin()
      .join(store.owner, owner).fetchJoin()
      .where(owner.id.eq(ownerId))
      .fetch();
  }
}
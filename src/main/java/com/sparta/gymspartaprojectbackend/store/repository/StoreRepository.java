package com.sparta.gymspartaprojectbackend.store.repository;

import com.sparta.gymspartaprojectbackend.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

  List<Store> findAllByOwnerAccountId(String accountId);

  @Query("SELECT s FROM Store s WHERE s.storeName LIKE %:keyword% OR s.address LIKE %:keyword%")
  List<Store> findByKeyword(@Param("keyword") String keyword);
}
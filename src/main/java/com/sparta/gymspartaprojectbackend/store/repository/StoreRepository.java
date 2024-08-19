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

  @Query("SELECT s FROM Store s WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(s.latitude)) * cos(radians(s.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(s.latitude)))) < :radius AND (s.storeName LIKE %:keyword% OR s.address LIKE %:keyword%)")
  List<Store> findByLocationAndKeyword(@Param("latitude") Double latitude, @Param("longitude") Double longitude, @Param("keyword") String keyword, @Param("radius") Double radius);

  @Query("SELECT s FROM Store s WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(s.latitude)) * cos(radians(s.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(s.latitude)))) < :radius")
  List<Store> findByLocation(@Param("latitude") Double latitude, @Param("longitude") Double longitude, @Param("radius") Double radius);

  @Query("SELECT s FROM Store s WHERE s.storeName LIKE %:keyword% OR s.address LIKE %:keyword%")
  List<Store> findByKeyword(@Param("keyword") String keyword);
}
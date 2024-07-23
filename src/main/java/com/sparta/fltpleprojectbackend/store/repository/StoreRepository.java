package com.sparta.fltpleprojectbackend.store.repository;

import com.sparta.fltpleprojectbackend.store.entity.Store;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Store 엔티티에 대한 Repository.
 */
@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

  List<Store> findAllByUserId(Long userId);
}

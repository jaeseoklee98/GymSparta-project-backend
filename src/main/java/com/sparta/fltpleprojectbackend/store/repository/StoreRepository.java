package com.sparta.fltpleprojectbackend.store.repository;

import com.sparta.fltpleprojectbackend.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Store 엔티티에 대한 Repository.
 */

public interface StoreRepository extends JpaRepository<Store, Long> {

}

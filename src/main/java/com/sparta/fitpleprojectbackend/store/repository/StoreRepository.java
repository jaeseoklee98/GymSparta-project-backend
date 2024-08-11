package com.sparta.fitpleprojectbackend.store.repository;

import com.sparta.fitpleprojectbackend.store.entity.Store;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

  List<Store> findAllByOwnerAccountId(String accountId);
}

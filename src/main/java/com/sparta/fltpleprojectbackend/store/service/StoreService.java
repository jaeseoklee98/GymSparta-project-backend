package com.sparta.fltpleprojectbackend.store.service;

import com.sparta.fltpleprojectbackend.store.dto.StoreRequest;
import com.sparta.fltpleprojectbackend.store.dto.StoreResponse;
import com.sparta.fltpleprojectbackend.store.entity.Store;
import com.sparta.fltpleprojectbackend.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 매장 관련 작업을 처리하는 서비스 클래스.
 */
@Service
@RequiredArgsConstructor
public class StoreService {

  private final StoreRepository storeRepository;

  /**
   * 1. 매장 등록
   *
   * @param request 등록할 매장의 세부 정보
   * @return 등록된 매장의 세부 정보를 포함하는 응답 객체
   */
  public StoreResponse createStore(StoreRequest request) {

    Store store = new Store(request);
    storeRepository.save(store);
    return new StoreResponse(store);
  }

  /**
   * 2. 매장 정보 수정
   *
   * @param storeId 수정하려는 매장의 id 값
   * @param request 수정할 매장의 세부 정보
   * @return 수정된 매장의 세부 정보를 포함하는 응답 객체
   */
  @Transactional
  public StoreResponse updateStore(Long storeId, StoreRequest request) {
    Store store = findStoreById(storeId);

    store.update(request);
    return new StoreResponse(store);
  }

  public void deleteStore(Long storeId) {
    Store store = findStoreById(storeId);

    storeRepository.delete(store);
  }

  private Store findStoreById(long id) {
    return storeRepository.findById(id)
        .orElseThrow(()-> new IllegalArgumentException("해당 매장이 존재하지 않습니다."));
  }
}
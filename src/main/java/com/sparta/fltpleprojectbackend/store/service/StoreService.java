package com.sparta.fltpleprojectbackend.store.service;

import com.sparta.fltpleprojectbackend.store.dto.StoreRequest;
import com.sparta.fltpleprojectbackend.store.dto.StoreResponse;
import com.sparta.fltpleprojectbackend.store.entity.Store;
import com.sparta.fltpleprojectbackend.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}

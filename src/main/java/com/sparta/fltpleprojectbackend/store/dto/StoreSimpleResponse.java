package com.sparta.fltpleprojectbackend.store.dto;

import com.sparta.fltpleprojectbackend.store.entity.Store;
import lombok.Getter;

/**
 * 매장 정보를 응답하기 위한 DTO.
 */
@Getter
public class StoreSimpleResponse {

  private Long storeId;

  private String storeName;


  /**
   * Store 엔티티를 기반으로 StoreResponse 객체를 생성.
   *
   * @param store Store 엔티티
   */
  public StoreSimpleResponse(Store store) {
    this.storeId = store.getId();
    this.storeName = store.getStoreName();
  }
}

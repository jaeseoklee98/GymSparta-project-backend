package com.sparta.fltpleprojectbackend.store.dto;

import com.sparta.fltpleprojectbackend.store.entity.Store;
import lombok.Getter;

/**
 * 매장 정보를 응답하기 위한 DTO.
 */
@Getter
public class StoreResponse {

  private String storeName;

  private String address;

  private String streetAddress;

  private String postalCode;

  private String storeInfo;

  private String storeHour;

  private String storeTel;

  /**
   * Store 엔티티를 기반으로 StoreResponse 객체를 생성.
   *
   * @param store Store 엔티티
   */
  public StoreResponse(Store store) {
    this.storeName = store.getStoreName();
    this.address = store.getAddress();
    this.streetAddress = store.getStreetAddress();
    this.postalCode = store.getPostalCode();
    this.storeInfo = store.getStoreInfo();
    this.storeHour = store.getStoreHour();
    this.storeTel = store.getStoreTel();
  }
}

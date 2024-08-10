package com.sparta.fitpleprojectbackend.store.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 매장 정보를 요청하기 위한 DTO.
 */
@Getter
@NoArgsConstructor
public class StoreRequest {

  private String storeName;

  private String address;

  private String streetAddress;

  private String postalCode;

  private String storeInfo;

  private String storeHour;

  private String storeTel;

}

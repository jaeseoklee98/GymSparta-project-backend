package com.sparta.fitpleprojectbackend.store.dto;

import com.sparta.fitpleprojectbackend.store.entity.Store;
import java.util.List;
import lombok.Getter;

@Getter
public class StoreResponse {

  private Long id;

  private String storeName;

  private String address;

//  private String streetAddress;
//
//  private String postalCode;

  private String storeInfo;

  private String storeHour;

  private String storeTel;

  private String price;

  private String image;

  private List<String> reviews;

  private List<String> memberships;

  private List<String> services;

  /**
   * Store 엔티티를 기반으로 StoreResponse 객체를 생성.
   *
   * @param store Store 엔티티
   */
  public StoreResponse(Store store) {
    this.id = store.getId();
    this.storeName = store.getStoreName();
    this.address = store.getAddress();
//    this.streetAddress = store.getStreetAddress();
//    this.postalCode = store.getPostalCode();
    this.storeInfo = store.getStoreInfo();
    this.storeHour = store.getStoreHour();
    this.storeTel = store.getStoreTel();
    this.price = store.getPrice();
    this.image = store.getImage();
    this.reviews = store.getReviews();
    this.memberships = store.getMemberships();
    this.services = store.getServices();
  }
}

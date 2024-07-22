package com.sparta.fltpleprojectbackend.store.entity;

import com.sparta.fltpleprojectbackend.common.TimeStamped;
import com.sparta.fltpleprojectbackend.store.dto.StoreRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Store 엔티티 클래스. 매장 정보를 데이터베이스에 저장하기 위한 클래스.
 */
@Getter
@Entity
@NoArgsConstructor
public class Store extends TimeStamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String storeName;

  @Column(nullable = false)
  private String address;

  @Column(nullable = false)
  private String streetAddress;

  @Column(unique = true, nullable = false)
  private String postalCode;

  @Column(nullable = false)
  private String storeInfo;

  @Column(nullable = false)
  private String storeHour;

  @Column(nullable = false)
  private String storeTel;

  /**
   * StoreRequest 객체를 사용하여 Store 엔티티를 생성하는 생성자.
   *
   * @param request 매장 생성에 필요한 정보를 담고 있는 StoreRequest 객체.
   */
  public Store(StoreRequest request) {
    this.storeName = request.getStoreName();
    this.address = request.getAddress();
    this.streetAddress = request.getStreetAddress();
    this.postalCode = request.getPostalCode();
    this.storeInfo = request.getStoreInfo();
    this.storeHour = request.getStoreHour();
    this.storeTel = request.getStoreTel();
  }

  /**
   * StoreRequest 객체를 사용하여 기존 Store 엔티티의 정보를 업데이트합니다.
   *
   * @param request 매장 정보 수정에 필요한 정보를 담고 있는 StoreRequest 객체.
   */
  public void update(StoreRequest request) {
    this.storeName = request.getStoreName();
    this.address = request.getAddress();
    this.streetAddress = request.getStreetAddress();
    this.postalCode = request.getPostalCode();
    this.storeInfo = request.getStoreInfo();
    this.storeHour = request.getStoreHour();
    this.storeTel = request.getStoreTel();
  }
}

package com.sparta.gymspartaprojectbackend.store.entity;

import com.sparta.gymspartaprojectbackend.common.TimeStamped;
import com.sparta.gymspartaprojectbackend.owner.entity.Owner;
import com.sparta.gymspartaprojectbackend.store.dto.StoreRequest;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
  private String storeInfo;

  @Column(nullable = false)
  private String storeHour;

  @Column(nullable = false)
  private String storeTel;

  @Column(nullable = false)
  private Double latitude;  // 위도 필드 추가

  @Column(nullable = false)
  private Double longitude;  // 경도 필드 추가

  @ElementCollection
  private List<String> services;

  @ElementCollection
  private List<String> memberships;

  @ElementCollection
  private List<String> ptConsultations;

  @ElementCollection
  private List<String> trainerList;

  @Column
  private String price;

  @Column(nullable = true)
  private String image;

  @ElementCollection
  private List<String> reviews;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner_id", nullable = false)
  private Owner owner;

  public Store(String storeName, String address, String storeInfo, String storeHour, String storeTel,
      String price, String image, Owner owner, Double latitude, Double longitude) {
    this.storeName = storeName;
    this.address = address;
    this.storeInfo = storeInfo;
    this.storeHour = storeHour;
    this.storeTel = storeTel;
    this.price = price;
    this.image = image;
    this.owner = owner;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public Store(StoreRequest request, Owner owner) {
    this.owner = owner;
    this.storeName = request.getStoreName();
    this.address = request.getAddress();
    this.storeInfo = request.getStoreInfo();
    this.storeHour = request.getStoreHour();
    this.storeTel = request.getStoreTel();
    this.services = request.getServices();
    this.memberships = request.getMemberships();
    this.ptConsultations = request.getPtConsultations();
    this.trainerList = request.getTrainerList();
    this.price = request.getPrice();
    this.image = request.getImage();
    this.reviews = request.getReviews();
    this.latitude = request.getLatitude();  // 추가 필드
    this.longitude = request.getLongitude();  // 추가 필드
  }

  public void update(StoreRequest request) {
    this.storeName = request.getStoreName();
    this.address = request.getAddress();
    this.storeInfo = request.getStoreInfo();
    this.storeHour = request.getStoreHour();
    this.storeTel = request.getStoreTel();
    this.services = request.getServices();
    this.memberships = request.getMemberships();
    this.ptConsultations = request.getPtConsultations();
    this.trainerList = request.getTrainerList();
    this.price = request.getPrice();
    this.image = request.getImage();
    this.reviews = request.getReviews();
    this.latitude = request.getLatitude();  // 추가 필드
    this.longitude = request.getLongitude();  // 추가 필드
  }
}
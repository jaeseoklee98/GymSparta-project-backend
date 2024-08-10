package com.sparta.fitpleprojectbackend.store.entity;

import com.sparta.fitpleprojectbackend.common.TimeStamped;
import com.sparta.fitpleprojectbackend.owner.entity.Owner;
import com.sparta.fitpleprojectbackend.store.dto.StoreRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
  private String streetAddress;

  @Column(nullable = false)
  private String postalCode;

  @Column(nullable = false)
  private String storeInfo;

  @Column(nullable = false)
  private String storeHour;

  @Column(nullable = false)
  private String storeTel;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner_id", nullable = false)
  private Owner owner;


  public Store(StoreRequest request, Owner owner) {
    this.owner = owner;
    this.storeName = request.getStoreName();
    this.address = request.getAddress();
    this.streetAddress = request.getStreetAddress();
    this.postalCode = request.getPostalCode();
    this.storeInfo = request.getStoreInfo();
    this.storeHour = request.getStoreHour();
    this.storeTel = request.getStoreTel();
  }

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

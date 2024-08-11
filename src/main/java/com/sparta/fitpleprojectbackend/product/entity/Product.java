package com.sparta.fitpleprojectbackend.product.entity;

import com.sparta.fitpleprojectbackend.common.TimeStamped;
import com.sparta.fitpleprojectbackend.payment.enums.ProductType;
import com.sparta.fitpleprojectbackend.store.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Product extends TimeStamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long productId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "store_id", nullable = false)
  private Store store;

  @Column(nullable = false)
  private String productName;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ProductType productType;

  @Column(nullable = false)
  private double productPrice;

  @Column(nullable = false)
  private int durationOrSessions;

  @Column
  private String description;

  // Constructor
  public Product(Store store, String productName, ProductType productType, double productPrice, int durationOrSessions, String description) {
    this.store = store;
    this.productName = productName;
    this.productType = productType;
    this.productPrice = productPrice;
    this.durationOrSessions = durationOrSessions;
    this.description = description;
  }
}
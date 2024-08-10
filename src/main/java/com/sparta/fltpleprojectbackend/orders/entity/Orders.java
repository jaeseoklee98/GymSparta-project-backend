package com.sparta.fltpleprojectbackend.orders.entity;

import com.sparta.fltpleprojectbackend.common.TimeStamped;
import com.sparta.fltpleprojectbackend.product.entity.Product;
import com.sparta.fltpleprojectbackend.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "orders") // 매핑할 테이블의 이름을 지정
@NoArgsConstructor
public class Orders extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

}

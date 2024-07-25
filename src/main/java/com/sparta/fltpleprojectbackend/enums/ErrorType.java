package com.sparta.fltpleprojectbackend.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {
  REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다."),
  REVIEW_MODIFICATION_PERIOD_EXPIRED(HttpStatus.BAD_REQUEST, "리뷰 수정 가능 기간이 지났습니다."),
  USER_NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "사용자가 권한이 없습니다."),
  ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."),
  PRODUCT_NOT_PURCHASED(HttpStatus.NOT_FOUND, "상품 구매 내역이 없습니다."),
  NOT_FOUND_USER(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
  ;

  private final HttpStatus httpStatus;
  private final String message;

  ErrorType(HttpStatus httpStatus, String message) {
    this.httpStatus = httpStatus;
    this.message = message;
  }
}

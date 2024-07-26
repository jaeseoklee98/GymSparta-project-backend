package com.sparta.fltpleprojectbackend.common;

import lombok.Getter;

/**
 * API 응답을 표준화하기 위한 제네릭 클래스.
 *
 * @param <T> 응답 데이터의 삽입.
 */
@Getter
public class CommonResponse<T> {

  private Integer statusCode;
  private String message;
  private T data;

  /**
   * CommonResponse 객체 생성자.
   *
   * @param statusCode HTTP 상태 코드
   * @param message 응답 메시지
   * @param data 응답 데이터
   */
  public CommonResponse(Integer statusCode, String message, T data) {
    this.statusCode = statusCode;
    this.message = message;
    this.data = data;
  }
}
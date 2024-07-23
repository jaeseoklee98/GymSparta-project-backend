package com.sparta.fltpleprojectbackend.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {

  NOT_FOUND_USER(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
  ;

  private final HttpStatus httpStatus;
  private final String message;

  ErrorType(HttpStatus httpStatus, String message) {
    this.httpStatus = httpStatus;
    this.message = message;
  }
}

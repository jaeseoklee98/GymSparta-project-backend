package com.sparta.fltpleprojectbackend.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {

  NOT_FOUND_USER(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),

  DUPLICATE_USER(HttpStatus.BAD_REQUEST,"중복된 사용자 정보입니다."),
  DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 사용자 아이디입니다."),
  DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
  INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
  INVALID_INPUT(HttpStatus.BAD_REQUEST, "잘못된 입력입니다."),

  // 점주
  NOT_FOUND_OWNER(HttpStatus.NOT_FOUND, "점주를 찾을 수 없습니다.");


  private final HttpStatus httpStatus;
  private final String message;

  ErrorType(HttpStatus httpStatus, String message) {
    this.httpStatus = httpStatus;
    this.message = message;
  }

}
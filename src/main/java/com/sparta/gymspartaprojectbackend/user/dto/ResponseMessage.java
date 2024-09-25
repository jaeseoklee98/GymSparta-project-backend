package com.sparta.gymspartaprojectbackend.user.dto;

import lombok.Getter;

@Getter
public class ResponseMessage<T> {

  private String result;

  private String message;

  public ResponseMessage(String result, String message) {
    this.result = result;
    this.message = message;
  }

  public static <T> ResponseMessage<T> success(String message) {
    return new ResponseMessage<>("SUCCESS", message);
  }

  public static <T> ResponseMessage<T> error(String message) {
    return new ResponseMessage<>("ERROR", message);
  }
}
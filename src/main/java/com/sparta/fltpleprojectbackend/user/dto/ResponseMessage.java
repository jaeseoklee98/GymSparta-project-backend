package com.sparta.fltpleprojectbackend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseMessage<T> {
  private int statusCode;
  private String message;
  private T data;

  public static <T> ResponseMessage<T> success(String message, T data) {
    return new ResponseMessage<>(200, message, data);
  }

  public static <T> ResponseMessage<T> error(String message, T data) {
    return new ResponseMessage<>(400, message, data);
  }
}
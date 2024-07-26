package com.sparta.fltpleprojectbackend.user.exception;

import com.sparta.fltpleprojectbackend.enums.ErrorType;
import com.sparta.fltpleprojectbackend.exception.CustomException;

public class UserException extends CustomException {

  public UserException(ErrorType errorType) {

    super(errorType, "주민등록번호 또는 외국인등록번호 중 하나는 필수 항목입니다.");
  }
}
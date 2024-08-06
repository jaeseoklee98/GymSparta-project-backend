package com.sparta.fitpleprojectbackend.exception;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * CustomException
   *
   * @param e 발생한 CustomException 예외
   * @return CustomException에 대한 응답
   */
  @ExceptionHandler(CustomException.class)
  public ResponseEntity<?> handleScheduleException(CustomException e) {
    log.error("에러 메세지: ", e);
    return ResponseEntity.status(e.getErrorType().getHttpStatus()).body(new ExceptionDto(e.getErrorType()));
  }

  /**
   * MethodArgumentNotValidException
   *
   * @param e 발생한 MethodArgumentNotValidException 예외
   * @return 유효성 검사 실패에 대한 응답
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleException(MethodArgumentNotValidException e) {

    log.error("에러 메세지: ", e);
    BindingResult bindingResult = e.getBindingResult();
    StringBuilder builder = new StringBuilder();

    for (FieldError fieldError : bindingResult.getFieldErrors()) {
      builder.append(fieldError.getField()).append(" : ").append(fieldError.getDefaultMessage()).append("\n");
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDto(builder.toString()));
  }

  /**
   * SQLIntegrityConstraintViolationException
   *
   * @param e 발생한 SQLIntegrityConstraintViolationException 예외
   * @return 데이터베이스 제약 조건 위반에 대한 응답
   */
  @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
  public ResponseEntity<Map<String, String>> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
    log.error("에러 메세지: ", e);
    Map<String, String> errorResponse = new HashMap<>();
    String message = e.getMessage();

    if (message.contains("Duplicate entry") && message.contains("for key 'store.UKs1wrksusdh1r9nhoau7qxggjn'")) {
      errorResponse.put("message", "중복된 매장명입니다. 다른 매장명을 입력하세요.");
    } else {
      errorResponse.put("message", "에러 발생: " + message);
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }
}
package com.sparta.fltpleprojectbackend.store.controller;

import com.sparta.fltpleprojectbackend.common.CommonResponse;
import com.sparta.fltpleprojectbackend.store.dto.StoreRequest;
import com.sparta.fltpleprojectbackend.store.dto.StoreResponse;
import com.sparta.fltpleprojectbackend.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 매장 CRUD 를 위한 Controller.
 */
@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

  private final StoreService storeService;

  /**
   * 1. 매장 등록
   *
   * @param request 매장 등록 시 필요한 정보를 입력
   * @return HTTP 상태 코드, 응답 메시지, 응답 데이터
   */
  @PostMapping("/admin")
  public ResponseEntity<CommonResponse<StoreResponse>> createStore(
      @Valid @RequestBody StoreRequest request
  ) {
    StoreResponse storeResponse = storeService.createStore(request);
    CommonResponse<StoreResponse> response = new CommonResponse<>(
        HttpStatus.CREATED.value(), "매장 등록 완료", storeResponse);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }
}

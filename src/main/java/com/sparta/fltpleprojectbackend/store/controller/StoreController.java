package com.sparta.fltpleprojectbackend.store.controller;

import com.sparta.fltpleprojectbackend.common.CommonResponse;
import com.sparta.fltpleprojectbackend.security.UserDetailsImpl;
import com.sparta.fltpleprojectbackend.store.dto.StoreRequest;
import com.sparta.fltpleprojectbackend.store.dto.StoreResponse;
import com.sparta.fltpleprojectbackend.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
   * @param userDetails 인증된 사용자의 정보
   * @param request     매장 등록에 필요한 정보를 담고 있는 DTE
   * @return HTTP 상태 코드, 응답 메시지, 응답 데이터
   */
  @PostMapping("/admin")
  public ResponseEntity<CommonResponse<StoreResponse>> createStore(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @Valid @RequestBody StoreRequest request
  ) {
    StoreResponse storeResponse = storeService.createStore(request, userDetails.getUser());
    CommonResponse<StoreResponse> response = new CommonResponse<>(
        HttpStatus.CREATED.value(), "매장 등록 완료", storeResponse);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  /**
   * 2. 매장 정보 수정
   *
   * @param storeId     수정할 매장의 ID
   * @param userDetails 인증된 사용자의 정보
   * @param request     매장 정보 수정에 필요한 정보를 담고 있는 DTO
   * @return HTTP 상태코드, 응답 메시지, 응답 데이터
   */
  @PutMapping("/admin/{storeId}")
  public ResponseEntity<CommonResponse<StoreResponse>> updateStore(
      @PathVariable Long storeId,
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @Valid @RequestBody StoreRequest request
  ) {
    StoreResponse storeResponse = storeService.updateStore(storeId, request, userDetails.getUser());
    CommonResponse<StoreResponse> response = new CommonResponse<>(
        HttpStatus.OK.value(), "매장 수정 완료", storeResponse);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  /**
   * 3. 매장 삭제
   *
   * @param userDetails 인증된 사용자의 정보
   * @param storeId     삭제할 매장의 ID
   * @return HTTP 상태 코드
   */
  @DeleteMapping("/admin/{storeId}")
  public ResponseEntity<CommonResponse<StoreResponse>> deleteStore(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable Long storeId
  ) {
    storeService.deleteStore(storeId, userDetails.getUser());
    return new ResponseEntity<>(HttpStatus.OK);
  }
}

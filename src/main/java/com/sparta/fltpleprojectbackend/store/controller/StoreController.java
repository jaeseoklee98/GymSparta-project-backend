package com.sparta.fltpleprojectbackend.store.controller;

import com.sparta.fltpleprojectbackend.common.CommonResponse;
import com.sparta.fltpleprojectbackend.security.UserDetailsImpl;
import com.sparta.fltpleprojectbackend.store.dto.StoreRequest;
import com.sparta.fltpleprojectbackend.store.dto.StoreResponse;
import com.sparta.fltpleprojectbackend.store.dto.StoreSimpleResponse;
import com.sparta.fltpleprojectbackend.store.service.StoreService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

  private final StoreService storeService;

  /**
   * 1. 매장 등록
   *
   * @param userDetails 인증된 사용자의 정보
   * @param request     매장 등록에 필요한 정보를 담고 있는 DTO
   * @return HTTP 상태 코드, 응답 메시지, 응답 데이터(매장 명, 매장 정보 등 매장 상세 정보)
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
   * @return HTTP 상태코드, 응답 메시지, 응답 데이터(매장 명, 매장 정보 등 매장 상세 정보)
   */
  @PutMapping("/admin/{storeId}")
  public ResponseEntity<CommonResponse<StoreResponse>> updateStore(
      @PathVariable Long storeId,
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @Valid @RequestBody StoreRequest request
  ) {
    StoreResponse storeResponse = storeService.updateStore(storeId, request, userDetails.getUser().getAccountId());
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
    storeService.deleteStore(storeId, userDetails.getUser().getAccountId());
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * 4. 매장 전체 조회
   *
   * @return HTTP 상태 코드, 응답 메시지, 응답 데이터(매장 ID, 매장 명)
   */
  @GetMapping
  public ResponseEntity<CommonResponse<List<StoreSimpleResponse>>> findAllStore() {
    List<StoreSimpleResponse> stores = storeService.findAll();

    CommonResponse<List<StoreSimpleResponse>> response = new CommonResponse<>(
        HttpStatus.OK.value(), "매장 전체 조회 완료", stores);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  /**
   * 5. 매장 상세 조회
   *
   * @param storeId 조회하고자 하는 매장 ID
   * @return HTTP 상태 코드, 응답 메시지, 응답 데이터(조회한 매장의 상세 정보)
   */
  @GetMapping("/{storeId}")
  public ResponseEntity<CommonResponse<StoreResponse>> findStore(
      @PathVariable Long storeId
  ) {
    StoreResponse storeResponse = storeService.findById(storeId);

    CommonResponse<StoreResponse> response = new CommonResponse<>(
        HttpStatus.OK.value(), "매장 상세 조회 완료", storeResponse);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  /**
   * 6. 점주 등록된 매장 조회
   *
   * @param userDetails 조회하고자 하는 점주의 사용자 정보
   * @return HTTP 상태 코드, 응답 메시지, 응답 데이터(해당 점주가 등록한 매장의 ID와 이름)
   */
  @GetMapping("/admin")
  public ResponseEntity<CommonResponse<List<StoreSimpleResponse>>> findAllAdminStore(
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    List<StoreSimpleResponse> stores = storeService.findAllAdmin(userDetails.getUser().getAccountId());

    CommonResponse<List<StoreSimpleResponse>> response = new CommonResponse<>(
        HttpStatus.OK.value(), "점주 매장 조회 완료", stores);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/admin/{storeId}")
  public ResponseEntity<CommonResponse<StoreResponse >> findAdminStore(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable Long storeId
  ) {
    StoreResponse storeResponse = storeService.findAdminById(userDetails.getUser().getAccountId(), storeId);

    CommonResponse<StoreResponse> response = new CommonResponse<>(
        HttpStatus.OK.value(), "점주 매장 상세 조회 완료", storeResponse);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}

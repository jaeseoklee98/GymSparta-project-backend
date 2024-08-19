package com.sparta.gymspartaprojectbackend.store.service;

import com.sparta.gymspartaprojectbackend.enums.ErrorType;
import com.sparta.gymspartaprojectbackend.owner.entity.Owner;
import com.sparta.gymspartaprojectbackend.store.dto.StoreRequest;
import com.sparta.gymspartaprojectbackend.store.dto.StoreResponse;
import com.sparta.gymspartaprojectbackend.store.dto.StoreSimpleResponse;
import com.sparta.gymspartaprojectbackend.store.entity.Store;
import com.sparta.gymspartaprojectbackend.store.exception.StoreException;
import com.sparta.gymspartaprojectbackend.store.repository.StoreRepository;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class StoreService {

  private final StoreRepository storeRepository;

  /**
   * 매장 등록
   *
   * @param request 등록할 매장의 세부 정보
   * @return 등록된 매장의 세부 정보를 포함하는 응답 객체
   */
  public StoreResponse createStore(StoreRequest request, Owner owner) {
    if (owner == null) {
      throw new StoreException(ErrorType.FORBIDDEN_OPERATION);
    }
    Store store = new Store(request, owner);
    storeRepository.save(store);
    return new StoreResponse(store);
  }

  /**
   * 매장 정보 수정
   *
   * @param storeId 수정하려는 매장의 id 값
   * @param request 수정할 매장의 세부 정보
   * @return 수정된 매장의 세부 정보를 포함하는 응답 객체
   */
  @Transactional
  public StoreResponse updateStore(Long storeId, StoreRequest request, String accountId) {
    Store store = findStoreById(storeId);

    validateUser(store, accountId);

    store.update(request);
    return new StoreResponse(store);
  }

  /**
   * 매장 삭제
   *
   * @param storeId 삭제하려는 매장의 id 값
   */
  public void deleteStore(Long storeId, String accountId) {
    Store store = findStoreById(storeId);

    validateUser(store, accountId);

    storeRepository.delete(store);
  }

  /**
   * 매장 전체 조회
   *
   * @return 등록된 모든 매장의 ID와 매장 명
   */
  public List<StoreSimpleResponse> findAll() {
    List<Store> storeList = storeRepository.findAll();
    return storeList.stream()
        .sorted(Comparator.comparing(Store::getCreatedAt))
        .map(StoreSimpleResponse::new)
        .toList();
  }

  /**
   * 매장 상세 조회
   *
   * @param storeId 상세 조회하고자하는 매장 ID
   * @return 상세 조회한 매장의 상세 정보
   */
  public StoreResponse findById(Long storeId) {
    Store store = findStoreById(storeId);

    return new StoreResponse(store);
  }

  /**
   * 점주가 등록한 매장 전체 조회
   *
   * @param accountId 점주의 ID
   * @return 점주가 등록한 모든 매장의 ID와 매장 명
   */
  public List<StoreSimpleResponse> findAllAdmin(String accountId) {
    List<Store> storeList = storeRepository.findAllByOwnerAccountId(accountId);
    return storeList.stream()
        .sorted(Comparator.comparing(Store::getCreatedAt))
        .map(StoreSimpleResponse::new)
        .toList();
  }

  /**
   * 점주가 등록한 매장의 상세 조회
   *
   * @param accountId 점주의 ID
   * @param storeId   조회하고자하는 매장의 ID
   * @return 조회한 매장의 상세 정보
   */
  public StoreResponse findAdminById(String accountId, Long storeId) {
    Store store = findStoreById(storeId);

    validateUser(store, accountId);

    return new StoreResponse(store);
  }

  private Store findStoreById(long id) {
    return storeRepository.findById(id)
        .orElseThrow(() -> new StoreException(ErrorType.NOT_FOUND_STORE));
  }

  private void validateUser(Store store, String accountId) {
    if (!store.getOwner().getAccountId().equals(accountId)) {
      throw new StoreException(ErrorType.INVALID_USER);
    }
  }

  public String updateRecentStoresCookie(String recentStores, Long storeId) {
    List<String> storeList = new ArrayList<>(Arrays.asList(recentStores.split(",")));

    // 중복된 매장 ID 제거
    storeList.remove(storeId.toString());

    // 최신 매장을 맨 앞에 추가
    storeList.add(0, storeId.toString());

    // 최근 방문 매장 리스트가 5개를 초과하지 않도록 제한
    if (storeList.size() > 5) {
      storeList = storeList.subList(0, 5);
    }

    // 쿠키 값 인코딩
    try {
      return URLEncoder.encode(String.join(",", storeList), StandardCharsets.UTF_8.toString());
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("Error encoding cookie value", e);
    }
  }

  public List<StoreSimpleResponse> findStoresByIds(String recentStores) {
    try {
      String decodedStores = URLDecoder.decode(recentStores, StandardCharsets.UTF_8.toString());

      List<Long> storeIds = Arrays.stream(decodedStores.split(","))
          .filter(id -> !id.trim().isEmpty())
          .map(Long::valueOf)
          .collect(Collectors.toList());

      if (storeIds.isEmpty()) {
        return new ArrayList<>();
      }

      List<Store> storeList = storeRepository.findAllById(storeIds);

      // 매장 목록을 쿠키에 저장된 순서대로 정렬
      Map<Long, Store> storeMap = storeList.stream()
          .collect(Collectors.toMap(Store::getId, store -> store));

      return storeIds.stream()
          .map(storeMap::get)
          .map(StoreSimpleResponse::new)
          .collect(Collectors.toList());
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("Error decoding cookie value", e);
    }
  }
}
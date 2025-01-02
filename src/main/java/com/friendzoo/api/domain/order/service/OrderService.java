package com.friendzoo.api.domain.order.service;


import com.friendzoo.api.domain.cart.dto.CartItemDTO;
import com.friendzoo.api.domain.order.controller.OrderController;
import com.friendzoo.api.domain.order.dto.OrderHistDTO;
import com.friendzoo.api.dto.PageResponseDTO;

import java.util.List;

public interface OrderService {

    /**
     * 주문 처리
     * @param cartItemDTOs 장바구니 상품 목록
     * @param email 회원 이메일
     * @return 주문 ID
     */
    Long order(List<CartItemDTO> cartItemDTOs, String email);

    /**
     * 주문 내역 조회
     * @param email 회원 이메일
     * @return 주문 내역 목록
     */
    List<OrderHistDTO> getOrders(String email);


    /**
     * 주문 내역 조회
     * @param email 회원 이메일
     * @param pageRequestDTO 페이징 정보
     * @return 주문 내역 목록
     */
    PageResponseDTO<OrderHistDTO> getOrdersPage(String email, OrderController.OrderHisRequestDTO pageRequestDTO);

    /**
     * 주문 유효성 검사
     * @param orderId 주문 ID
     * @param email 회원 이메일
     */
    void validateOrder(Long orderId, String email);

    /**
     * 주문 취소
     * @param orderId 주문 ID
     * @param email 회원 이메일
     */
    void cancelOrder(Long orderId, String email);


}

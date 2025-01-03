package com.friendzoo.api.domain.order.controller;

import com.friendzoo.api.domain.order.dto.OrderDTO;
import com.friendzoo.api.domain.order.dto.OrderHistDTO;
import com.friendzoo.api.domain.order.service.OrderService;
import com.friendzoo.api.dto.PageRequestDTO;
import com.friendzoo.api.dto.PageResponseDTO;
import com.friendzoo.api.security.MemberDTO;
import jakarta.validation.Valid;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 주문 처리
     *
     * @param memberDTO 로그인한 회원 정보
     * @param orderDTO  주문 정보
     * @return 주문 ID
     */
    @PostMapping
    public ResponseEntity<?> order(
            @AuthenticationPrincipal MemberDTO memberDTO,
            @Valid @RequestBody OrderDTO orderDTO
    ) {
        log.info("order orderDTO: {}", orderDTO);
        log.info("order memberDTO: {}", memberDTO);
        Long orderId = orderService.order(orderDTO.getCartItems(), memberDTO.getEmail());
        return ResponseEntity.ok("success order id: " + orderId);
    }


    @Setter
    @Getter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderHisRequestDTO extends PageRequestDTO {
        // 년도 ex. 2024, 2023, 2022 만약 null일시, 최근 6개월
        private Integer year;
    }

    /**
     * 주문 내역 조회
     *
     * @param memberDTO 로그인한 회원 정보
     * @return 주문 내역 목록
     */
    @GetMapping("/hist/list")
    public PageResponseDTO<OrderHistDTO> orderHist(
            OrderHisRequestDTO orderHisRequestDTO,
            @AuthenticationPrincipal MemberDTO memberDTO
    ) {
        log.info("getOrders orderHisRequestDTO: {}, memberDTO: {}", orderHisRequestDTO, memberDTO);
        return orderService.getOrdersPage(memberDTO.getEmail(), orderHisRequestDTO);
    }

    /**
     * 주문 취소
     *
     * @param memberDTO 로그인한 회원 정보
     * @param orderId   주문 ID
     * @return 취소 결과
     */
    @PostMapping("{id}/cancel")
    public ResponseEntity<String> cancelOrder(
            @AuthenticationPrincipal MemberDTO memberDTO,
            @PathVariable("id") Long orderId
    ) {
        log.info("cancelOrder memberDTO: {}", memberDTO);
        log.info("cancelOrder orderId: {}", orderId);
        orderService.validateOrder(orderId, memberDTO.getEmail());
        orderService.cancelOrder(orderId, memberDTO.getEmail());
        return ResponseEntity.ok("success cancel order id: " + orderId);
    }
}

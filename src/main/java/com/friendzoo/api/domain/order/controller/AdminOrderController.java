package com.friendzoo.api.domain.order.controller;

import com.friendzoo.api.domain.order.dto.OrderHistDTO;
import com.friendzoo.api.domain.order.service.OrderService;
import com.friendzoo.api.dto.PageRequestDTO;
import com.friendzoo.api.dto.PageResponseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/admin/order")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;


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
     * 주문 목록
     *
     * @return 주문 내역 목록
     */
    @GetMapping("/list")
    public PageResponseDTO<OrderHistDTO> orderList(
            OrderHisRequestDTO orderHisRequestDTO
    ) {
        log.info("orderList orderHisRequestDTO: {}", orderHisRequestDTO);
        return orderService.list(orderHisRequestDTO);
    }


}

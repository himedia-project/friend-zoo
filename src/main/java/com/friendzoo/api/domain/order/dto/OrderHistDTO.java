package com.friendzoo.api.domain.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.friendzoo.api.domain.order.entity.Order;
import com.friendzoo.api.domain.order.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderHistDTO {

    private Long orderId;       // 주문 아이디

    private String orderCode;   // 주문 코드

    private String email;       // 주문자 이메일

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime orderDate;    // 주문 날짜

    private OrderStatus orderStatus;    // 주문 상태

    private Integer totalPrice;         // 주문 총 금액

    @Builder.Default
    private List<OrderItemDTO> orderItems = new ArrayList<>();


    // order -> orderHistDTO
    public static OrderHistDTO from(Order order) {
        return OrderHistDTO.builder()
                .orderId(order.getId())
                .orderCode(order.getCode())
                .email(order.getMember().getEmail())
                .orderDate(order.getOrderDate())
                .orderStatus(order.getOrderStatus())
                .totalPrice(order.getTotalPrice())
                .build();
    }

    // 주문 상품 리스트 추가
    public void addOrderItemDto(OrderItemDTO orderItemDto) {
        orderItems.add(orderItemDto);
    }

}

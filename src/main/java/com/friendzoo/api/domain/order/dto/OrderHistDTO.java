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

    private Long orderId;   // 주문 아이디

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;   // 주문 날짜

    private OrderStatus orderStatus;   // 주문 상태

    private Integer totalPrice;  // 주문 총 금액

    @Builder.Default
    private List<OrderItemDTO> orderItems = new ArrayList<>();


    // order -> orderHistDTO
//    public OrderHistDto(Order order){
//        this.orderId = order.getId();
//        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
//        this.orderStatus = order.getOrderStatus();
//    }
    // 빌더
    public static OrderHistDTO from(Order order) {
        return OrderHistDTO.builder()
                .orderId(order.getId())
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

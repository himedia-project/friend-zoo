package com.friendzoo.api.domain.order.dto;

import com.friendzoo.api.domain.order.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDTO {

    private Long pno;       // 상품 번호
    private String pname;   // 상품명
    private int count;      // 주문 수량

    private int orderPrice; // 주문 가격
    private String imgUrl;  // 상품 이미지 URL


    // builder
    public static OrderItemDTO from(OrderItem orderItem, String imgUrl) {
        return OrderItemDTO.builder()
                .pno(orderItem.getProduct().getId())
                .pname(orderItem.getProduct().getName())
                .count(orderItem.getCount())
                .orderPrice(orderItem.getOrderPrice())
                .imgUrl(imgUrl)
                .build();
    }
}

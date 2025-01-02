package com.friendzoo.api.domain.cart.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartItemDTO {

    private Long cartItemId;

    private String email;

    // --- order request
    @NotNull(message = "상품 번호는 필수입니다.")
    private Long productId;

    @Max(value = 999, message = "최대 주문 수량은 999개입니다.")
    private int qty;


}

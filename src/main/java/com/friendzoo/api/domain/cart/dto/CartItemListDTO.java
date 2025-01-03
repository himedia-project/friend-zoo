package com.friendzoo.api.domain.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemListDTO {

    private Long cartItemId;
    private int qty;
    private Long productId;
    private String productName;
    private int price;
    private int discountPrice;
    private String imageName;


}

package com.friendzoo.api.domain.cart.service;

import com.friendzoo.api.domain.cart.dto.CartItemDTO;
import com.friendzoo.api.domain.cart.dto.CartItemListDTO;
import com.friendzoo.api.domain.cart.entity.CartItem;

import java.util.List;


public interface CartService {

    /**
     * 모든 장바구니 아이템 목록
     */

    List<CartItemListDTO> getCartItemList(String email);

    /**
     *  장바구니 아이템 추가 혹은 변경
     */

    List<CartItemListDTO> addOrModify(CartItemDTO cartItemDTO);


    /**
     *  장바구니 아이템 삭제
     */
    List<CartItemListDTO> remove(Long cino);


    /**
     * cartItem -> cartItemDTO 변환
     */
    default CartItemListDTO entityToDTO(CartItem cartItem) {

        return CartItemListDTO.builder()
                .cartItemId(cartItem.getId())
                .productId(cartItem.getProduct().getId())
                .productName(cartItem.getProduct().getName())
                .price(cartItem.getProduct().getPrice())
                .discountPrice(cartItem.getProduct().getDiscountPrice())
                .imageName(cartItem.getProduct().getImageList().get(0).getImageName())
                .qty(cartItem.getQty())
                .build();

    }

}

package com.friendzoo.api.domain.cart.repository.querydsl;

import com.friendzoo.api.domain.cart.entity.CartItem;

import java.util.List;

public interface CartItemRepositoryCustom {
    List<CartItem> getItemsOfCartList(String email);

    List<CartItem> getItemsOfCartByCartId(Long cartId);
}

package com.friendzoo.api.domain.cart.repository.querydsl;

import com.friendzoo.api.domain.cart.entity.CartItem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.friendzoo.api.domain.cart.entity.QCart.cart;
import static com.friendzoo.api.domain.cart.entity.QCartItem.cartItem;
import static com.friendzoo.api.domain.product.entity.QProduct.product;
import static com.friendzoo.api.domain.product.entity.QProductImage.productImage;

@Slf4j
@RequiredArgsConstructor
public class CartItemRepositoryImpl implements CartItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CartItem> getItemsOfCartList(String email) {

        return queryFactory
                .selectFrom(cartItem)
                .leftJoin(cartItem.cart, cart).fetchJoin()
                .leftJoin(cartItem.product, product).fetchJoin()
                .leftJoin(product.imageList, productImage).fetchJoin()
                .where(cart.member.email.eq(email).and(productImage.ord.eq(0)))
                .orderBy(cartItem.id.desc())
                .fetch();
    }

    @Override
    public List<CartItem> getItemsOfCartByCartId(Long cartId) {
        return queryFactory
                .selectFrom(cartItem)
                .leftJoin(cartItem.cart, cart).fetchJoin()
                .leftJoin(cartItem.product, product).fetchJoin()
                .leftJoin(product.imageList, productImage).fetchJoin()
                .where(cart.id.eq(cartId).and(productImage.ord.eq(0)))
                .orderBy(cartItem.id.desc())
                .fetch();
    }
}

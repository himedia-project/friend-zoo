package com.friendzoo.api.domain.cart.repository;

import com.friendzoo.api.domain.cart.entity.CartItem;
import com.friendzoo.api.domain.cart.repository.querydsl.CartItemRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long>
, CartItemRepositoryCustom {

/*
→ 특정한 사용자의 이메일을 통해서 해당 사용자의 모든 장바구니 아이템들을 조회 기능
→ 로그인 했을 때 사용자가 담은 모든 장바구니 아이템 조회시에 사용
→ 사용자의 이메일과 상품 번호로 해당 장바구니 아이템을 알아내는 기능
→ 새로운 상품을 장바구니에 담고자 할 때 기존 장바구니 아이템인지 확인하기 위해서 필요
→ 장바구니 아이템이 속한 장바구니의 번호를 알아내는 기능
→ 해당 아이템을 삭제한 후 해당 아이템이 속해 있는 장바구니의 모든 아이템을 조회
→ 특정한 장바구니의 번호만으로 해당 장바구니의 모든 장바구니 아이템들을 조회하는 기능
→ 특정한 장바구니 아이템을 삭제한 후에 해당 장바구니 아이템이 속해 있는 장바구니의 모든 장
바구니 아이템들을 조회할 때 필요
 */

    @Query("select ci from CartItem ci where ci.cart.member.email = :email and ci.product.id = :productId")
    CartItem getItemOfPno(@Param("email") String email, @Param("productId") Long productId );


    @Query("select ci.cart.id from CartItem ci where ci.id = :cino")
    Long getCartFromItem( @Param("cino") Long cino);

}

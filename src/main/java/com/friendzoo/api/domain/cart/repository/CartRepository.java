package com.friendzoo.api.domain.cart.repository;

import com.friendzoo.api.domain.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("select cart from Cart cart where cart.member.email = :email")
    Optional<Cart> getCartOfMember(@Param("email") String email);

}

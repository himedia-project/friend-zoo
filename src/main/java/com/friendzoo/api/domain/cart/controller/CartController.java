package com.friendzoo.api.domain.cart.controller;

import com.friendzoo.api.domain.cart.dto.CartItemDTO;
import com.friendzoo.api.domain.cart.dto.CartItemListDTO;
import com.friendzoo.api.domain.cart.service.CartService;
import com.friendzoo.api.security.MemberDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;


    // 장바구니 목록
//    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/item/list")
    public List<CartItemListDTO> getCartItems(Principal principal) {
        String email = principal.getName();
        log.info("--------------------------------------------");
        log.info("email: " + email );
        return cartService.getCartItemList(email);
    }

    // 장바구니에 상품 추가 혹은 변경
//    @PreAuthorize("#itemDTO.email == authentication.name")      // 사용자가 자신의 장바구니에만 접근 가능
    @PostMapping("/change")
    public List<CartItemListDTO> changeCart(
            @AuthenticationPrincipal MemberDTO memberDTO,
            @Valid @RequestBody CartItemDTO itemDTO
    ) {
        log.info("changeCart.......... memberDTO: {}, itemDTO: {}", memberDTO, itemDTO);
        itemDTO.setEmail(memberDTO.getEmail());
        if (itemDTO.getQty() <= 0) {
            return cartService.remove(itemDTO.getCartItemId());
        }
        return cartService.addOrModify(itemDTO);
    }

    // 장바구니 상품 삭제
//    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public List<CartItemListDTO> removeFromCart(
            @PathVariable("id") Long id
    ){
        log.info("cart item no: " + id);
        return cartService.remove(id);
    }
}

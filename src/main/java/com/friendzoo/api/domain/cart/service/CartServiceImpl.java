package com.friendzoo.api.domain.cart.service;

import com.friendzoo.api.domain.cart.dto.CartItemDTO;
import com.friendzoo.api.domain.cart.dto.CartItemListDTO;
import com.friendzoo.api.domain.cart.entity.Cart;
import com.friendzoo.api.domain.cart.entity.CartItem;
import com.friendzoo.api.domain.cart.repository.CartItemRepository;
import com.friendzoo.api.domain.cart.repository.CartRepository;
import com.friendzoo.api.domain.member.entity.Member;
import com.friendzoo.api.domain.product.entity.Product;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class CartServiceImpl implements CartService {


    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional(readOnly = true)
    @Override
    public List<CartItemListDTO> getCartItemList(String email) {
        log.info("getCartItemList..........");
        return cartItemRepository.getItemsOfCartList(email).stream()
                .map(this::entityToDTO)
                .toList();
    }


    @Override
    public List<CartItemListDTO> addOrModify(CartItemDTO cartItemDTO) {
        log.info("addOrModify..........");
        String email = cartItemDTO.getEmail();
        Long pno = cartItemDTO.getProductId();
        int qty = cartItemDTO.getQty();
        Long cartItemId = cartItemDTO.getCartItemId();

        if (cartItemId != null) { //장바구니 아이템 번호가 있어서 수량만 변경하는 경우
            CartItem cartItem = cartItemRepository.findById(cartItemId)
                    .orElseThrow(() -> new EntityNotFoundException("해당 장바구니가 존재하지 않습니다. cartItemId: " + cartItemId));
            cartItem.changeQty(qty);
            cartItemRepository.save(cartItem);
            return getCartItemList(email);
        }

        //장바구니 아이템 번호 cartItemId가 없는 경우
        //사용자의 카트
        Cart cart = this.getCart(email);
        CartItem cartItem = null;

        //이미 동일한 상품이 담긴적이 있을 수 있으므로
        cartItem = cartItemRepository.getItemOfPno(email, pno);
        if (cartItem == null) {
            Product product = Product.builder().id(pno).build();
            cartItem =
                    CartItem.builder().product(product).cart(cart).qty(qty).build();
        } else {
            cartItem.changeQty(qty);
        }
        //상품 아이템 저장
        cartItemRepository.save(cartItem);

        return List.of();
    }


    @Override
    public List<CartItemListDTO> remove(Long cino) {
        log.info("remove..........");
        Long cno = cartItemRepository.getCartFromItem(cino);

        log.info("cart cno: " + cno);

        cartItemRepository.deleteById(cino);

        return cartItemRepository.getItemsOfCartByCartId(cno).stream()
                .map(this::entityToDTO)
                .toList();
    }


    /**
     * 사용자의 장바구니가 없었다면 새로운 장바구니를 생성하고 반환
     *
     * @param email
     * @return Cart
     */
    private Cart getCart(String email) {

        Cart cart = null;

        Optional<Cart> result = cartRepository.getCartOfMember(email);
        if (result.isEmpty()) {
            log.info("Cart of the member is not exist!!");
            Member member = Member.builder().email(email).build();
            Cart tempCart = Cart.builder().member(member).build();
            cart = cartRepository.save(tempCart);
        } else {
            cart = result.get();
        }

        return cart;
    }
}

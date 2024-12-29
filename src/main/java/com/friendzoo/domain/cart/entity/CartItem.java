package com.friendzoo.domain.cart.entity;

import com.friendzoo.domain.product.entity.Product;
import com.friendzoo.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;


@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"cart"})
@Table(name = "cart_item", indexes = {
        @Index(name = "idx_cart_item", columnList = "cart_id"),
        @Index(name = "idx_cart_item_product", columnList = "product_id, cart_id")
})
public class CartItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    private int qty;

    public void changeQty(int qty) {
        this.qty = qty;
    }

}

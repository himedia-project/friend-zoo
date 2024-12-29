package com.friendzoo.api.domain.order.entity;

import com.friendzoo.api.domain.product.entity.Product;
import com.friendzoo.api.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_item")
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    private Order order;

    private int orderPrice;

    private int count;


    /**
     * 주문 상품 생성
     * @param product 주문상품
     * @param count 수량
     * @return OrderItem
     */
    public static OrderItem createOrderItem(Product product, int count) {
        OrderItem orderItem = new OrderItem();
//        orderItem.product = product;
//        orderItem.orderPrice = product.getPrice();
//        orderItem.count = count;
        orderItem.setProduct(product);
        orderItem.setOrderPrice(product.getPrice());
        orderItem.setCount(count);

        product.removeStock(count);
        return orderItem;
    }

    /**
     * 주문 상품의 총 가격 계산
     * @return 총 가격
     */
    public int getTotalPrice() {
        return orderPrice * count;
    }

    /**
     * 주문 취소
     * 재고 수량 복구
     */
    public void cancel() {
        // 재고 수량 복구
        this.product.addStock(count);
    }

}

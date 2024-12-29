package com.friendzoo.domain.order.entity;

import com.friendzoo.domain.member.entity.Member;
import com.friendzoo.domain.order.enums.OrderStatus;
import com.friendzoo.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 주문 날짜
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    // 주문 총 금액
    @ColumnDefault("0")
    private int totalPrice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default // 이거 안넣어주면, orderItems가 null이 되어버림
    private List<OrderItem> orderItems = new ArrayList<>();

    // order item 추가
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    // 주문 생성
    public static Order createOrder(Member member, List<OrderItem> orderItems) {
        Order order = Order.builder()
                .member(member)
                .orderDate(LocalDateTime.now())
                .orderStatus(OrderStatus.ORDER)
                .build();

        orderItems.forEach(order::addOrderItem);

        order.totalPrice = order.getCalcTotalPrice();

        return order;
    }

    // 총합 계산
    public int getCalcTotalPrice() {
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }

    // 주문 취소
    public void cancelOrder() {
        orderItems.forEach(OrderItem::cancel);
        this.orderStatus = OrderStatus.CANCEL;
    }

}

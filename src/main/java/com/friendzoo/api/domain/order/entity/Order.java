package com.friendzoo.api.domain.order.entity;

import com.friendzoo.api.domain.member.entity.Member;
import com.friendzoo.api.domain.order.enums.OrderStatus;
import com.friendzoo.api.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

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

    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email")
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
                .code(orderCodeGenerator())
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

    /**
     * 주문 코드 생성
     * @return 주문 코드 ex. 2021070112000001
     */
    private static String orderCodeGenerator() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = sdf.format(new Date());
        long count = new AtomicLong(0).incrementAndGet();
        return String.format("%s%04d", timestamp, count);
    }

}

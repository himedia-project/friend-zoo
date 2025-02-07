package com.friendzoo.api.domain.payment.entity;

import com.friendzoo.api.domain.payment.enums.PaymentStatus;
import com.friendzoo.api.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
@Table(name = "payment")
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;        // 주문 ID
    private String paymentKey;     // 결제 키
    private Long amount;           // 결제 금액

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'READY'")
    private PaymentStatus status;         // 결제 상태 (READY, DONE, CANCELED)


}

package com.friendzoo.api.domain.payment.dto;

import com.friendzoo.api.domain.payment.entity.Payment;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentPrepareDTO {

    private String orderId;
    private Long amount;

    public Payment toEntity() {
        return Payment.builder()
                .orderId(orderId)
                .amount(amount)
                .build();
    }
}

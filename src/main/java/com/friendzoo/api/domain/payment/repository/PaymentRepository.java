package com.friendzoo.api.domain.payment.repository;

import com.friendzoo.api.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT pm FROM Payment pm WHERE pm.orderId = :orderId")
    Optional<Payment> findByOrderId(@Param("orderId") String orderId);
}

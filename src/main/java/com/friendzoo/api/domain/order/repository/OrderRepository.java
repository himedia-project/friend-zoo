package com.friendzoo.api.domain.order.repository;

import com.friendzoo.api.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.member.email = :email")
    List<Order> findByEmail(@Param("email") String email);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.member.email = :email")
    Long countByEmail(@Param("email") String email);
}

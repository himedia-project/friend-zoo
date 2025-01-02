package com.friendzoo.api.domain.heart.repository;

import com.friendzoo.api.domain.heart.entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    @Query("select h from Heart h where h.productId = :id and h.email = :email")
    List<Heart> findrelatedItem(@Param("email") String email,
                                @Param("id") Long id);
}

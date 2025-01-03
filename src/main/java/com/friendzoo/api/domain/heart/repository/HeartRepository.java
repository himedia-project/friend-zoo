package com.friendzoo.api.domain.heart.repository;

import com.friendzoo.api.domain.heart.entity.Heart;
import com.friendzoo.api.domain.heart.repository.querydsl.HeartRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long>
, HeartRepositoryCustom {

    @Query("select h from Heart h where h.member.email = :email and h.product.id = :productId")
    Optional<Heart> findHeartProduct(@Param("email") String email, @Param("productId") Long productId);

    @Query("select case when count(h) = 1 then true else false end from Heart h where h.member.email = :email and h.content.id = :contentId")
    boolean findHeartContent(@Param("email") String email, @Param("contentId") Long contentId);

//    @Query("select case when count(h) = 1 then true else false end from Heart h where h.member.email = :email and h.content.id = :contentId")
//    boolean findDetailTagList(@Param("email") String email, @Param("contentId") Long contentId);
//    @Query("select h from Heart h where h.productId = :id and h.email = :email")
//    List<Heart> findrelatedItem(@Param("email") String email,
//                                @Param("id") Long id);

}

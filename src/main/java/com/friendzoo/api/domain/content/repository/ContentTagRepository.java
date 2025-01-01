package com.friendzoo.api.domain.content.repository;

import com.friendzoo.api.domain.content.entity.ContentTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContentTagRepository extends JpaRepository<ContentTag, Long> {

    @Modifying
    @Query("delete from ContentTag ct where ct.content.id = :contentId")
    void deleteByContent(@Param("contentId") Long contentId);
}

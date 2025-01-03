package com.friendzoo.api.domain.content.repository;

import com.friendzoo.api.domain.content.entity.Content;
import com.friendzoo.api.domain.content.repository.querydsl.ContentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long>
        , ContentRepositoryCustom {


    @Modifying
    @Query("update Content c set c.delFlag = true where c.id = :id")
    void modifyDeleteFlag(Long id);


    // content 내 카테고리 존재 여부 확인
    @Query("select case when count(c) > 0 then true else false end from Content c where c.division.id = :divisionId")
    boolean existsByDivisionId(@Param("divisionId") Long divisionId);
}

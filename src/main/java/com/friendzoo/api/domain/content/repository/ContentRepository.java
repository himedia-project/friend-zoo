package com.friendzoo.api.domain.content.repository;

import com.friendzoo.api.domain.content.entity.Content;
import com.friendzoo.api.domain.content.repository.querydsl.ContentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ContentRepository extends JpaRepository<Content, Long>
        , ContentRepositoryCustom {


    @Modifying
    @Query("update Content c set c.delFlag = true where c.id = :id")
    void modifyDeleteFlag(Long id);
}

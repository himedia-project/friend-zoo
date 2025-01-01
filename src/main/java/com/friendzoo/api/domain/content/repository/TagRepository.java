package com.friendzoo.api.domain.content.repository;

import com.friendzoo.api.domain.content.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("select case when count(t) > 0 then true else false end from Tag t where t.name = :name")
    boolean existsByName(@Param("name") String tag);

    @Query("select t from Tag t where t.name = :name")
    Optional<Tag> findByName(@Param("name") String tag);
}

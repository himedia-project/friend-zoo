package com.friendzoo.api.domain.content.repository;

import com.friendzoo.api.domain.content.entity.ContentTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentTagRepository extends JpaRepository<ContentTag, Long> {
}

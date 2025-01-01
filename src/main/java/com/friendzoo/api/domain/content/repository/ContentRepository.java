package com.friendzoo.api.domain.content.repository;

import com.friendzoo.api.domain.content.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Long> {
}

package com.friendzoo.api.domain.content.repository;

import com.friendzoo.api.domain.content.entity.Division;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DivisionRepository extends JpaRepository<Division, Long> {
}

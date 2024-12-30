package com.friendzoo.api.domain.test.repository;

import com.friendzoo.api.domain.test.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Long> {
}

package com.friendzoo.domain.test.repository;

import com.friendzoo.domain.test.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Long> {
}

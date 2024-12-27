package com.dsg.demo241226.domain.test.repository;

import com.dsg.demo241226.domain.test.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Long> {
}

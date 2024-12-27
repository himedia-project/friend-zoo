package com.dsg.demo241226.domain.user.repository;

import com.dsg.demo241226.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    @Query("select u from User u where u.email = :email") // leftjoin + fetch join (한번에 조회 ) Pagination OOM ->
    Optional<User> findByEmail(@Param("email") String email);
}

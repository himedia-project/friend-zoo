package com.friendzoo.api.domain.member.repository;

import com.friendzoo.api.domain.member.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {

}

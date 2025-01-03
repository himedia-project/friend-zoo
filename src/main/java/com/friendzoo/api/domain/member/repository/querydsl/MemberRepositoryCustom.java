package com.friendzoo.api.domain.member.repository.querydsl;

import com.friendzoo.api.domain.member.entity.Member;
import com.friendzoo.api.dto.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface MemberRepositoryCustom {

    Page<Member> findAllBy(PageRequestDTO requestDTO);
}

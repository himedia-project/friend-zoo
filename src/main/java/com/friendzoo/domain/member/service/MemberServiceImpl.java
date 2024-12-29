package com.friendzoo.domain.member.service;

import com.friendzoo.domain.member.dto.MemberDTO;
import com.friendzoo.domain.member.entity.Member;
import com.friendzoo.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    @Override
    public MemberDTO findUser(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
        return this.entityToDto(member);
    }
}

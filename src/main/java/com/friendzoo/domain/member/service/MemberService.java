package com.friendzoo.domain.member.service;

import com.friendzoo.domain.member.entity.Member;
import com.friendzoo.domain.test.dto.TestResDTO;
import com.friendzoo.domain.member.dto.MemberDTO;

public interface MemberService {
    MemberDTO findUser(String email);

    // user -> dto
    default MemberDTO entityToDto(Member member) {
        return MemberDTO.builder()
                .email(member.getEmail())
                .name(member.getName())
                .tests(member.getTestList().stream().map(test ->
                        TestResDTO.builder()
                                .id(test.getId())
                                .title(test.getTitle())
                                .createdAt(test.getCreatedAt())
                                .modifiedAt(test.getModifiedAt())
                                .build()
                ).toList())
                .build();
    }
}

package com.friendzoo.domain.member.service;

import com.friendzoo.domain.member.dto.JoinRequestDTO;
import com.friendzoo.domain.member.dto.MemberTestDTO;
import com.friendzoo.domain.member.entity.Member;
import com.friendzoo.domain.test.dto.TestResDTO;

import java.util.Map;


public interface MemberService {

    MemberTestDTO findUser(String email);

    void join(JoinRequestDTO request);

    Map<String, Object> login(String email, String password);

    // user -> dto
    default MemberTestDTO entityToDto(Member member) {
        return MemberTestDTO.builder()
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

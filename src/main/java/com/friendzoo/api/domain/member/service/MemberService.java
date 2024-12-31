package com.friendzoo.api.domain.member.service;

import com.friendzoo.api.domain.member.dto.JoinRequestDTO;
import com.friendzoo.api.domain.member.dto.MemberTestDTO;
import com.friendzoo.api.domain.member.entity.Member;
import com.friendzoo.api.domain.test.dto.TestResDTO;
import com.friendzoo.api.security.MemberDTO;

import java.util.Map;


public interface MemberService {

    MemberTestDTO findUser(String email);

    Member getMember(String email);

    void join(JoinRequestDTO request);

    Map<String, Object> login(String email, String password);

    /**
     * 회원 임시 비밀번호 발급
     *
     * @return 임시 비밀번호
     */
    String makeTempPassword();

    /**
     * 소셜 로그인 시 클레임 정보 반환
     * @param memberDTO 회원정보 DTO
     * @return 클레임 정보
     */
    Map<String, Object> getSocialClaims(MemberDTO memberDTO);

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

    /**
     * 회원정보 Entity -> DTO 변환
     *
     * @param member 회원정보
     * @return 회원정보 DTO
     */
    default MemberDTO entityToDTO(Member member) {

        return new MemberDTO(
                member.getEmail(),
                member.getPassword(),
                member.getName(),
                member.getMemberRoleList().stream()
                        .map(Enum::name).toList());
    }


}

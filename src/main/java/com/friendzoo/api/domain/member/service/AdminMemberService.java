package com.friendzoo.api.domain.member.service;

import com.friendzoo.api.domain.member.dto.MemberResDTO;
import com.friendzoo.api.domain.member.entity.Member;
import com.friendzoo.api.dto.PageRequestDTO;
import com.friendzoo.api.dto.PageResponseDTO;

public interface AdminMemberService {

    PageResponseDTO<MemberResDTO> getList(PageRequestDTO requestDTO);

    default MemberResDTO entityToDTO(Member member){

        return MemberResDTO.builder()
                .email(member.getEmail())
                .name(member.getName())
                .phone(member.getPhone())
                .roles(member.getMemberRoleList())
                .createdAt(member.getCreatedAt())
                .modifiedAt(member.getModifiedAt())
                .build();
    }
}

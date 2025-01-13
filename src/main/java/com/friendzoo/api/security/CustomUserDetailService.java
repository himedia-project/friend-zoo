package com.friendzoo.api.security;

import com.friendzoo.api.domain.member.entity.Member;
import com.friendzoo.api.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("loadUserByUsername: username: {}", username);

        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Check Email or Social"));

        MemberDTO memberDTO = new MemberDTO(
                member.getEmail(),
                member.getPassword(),
                member.getName(),
                member.getMemberRoleList().stream().map(Enum::name).toList());

        log.info("loadUserByUsername result memberDTO: {}", memberDTO);

        return memberDTO;
    }
}

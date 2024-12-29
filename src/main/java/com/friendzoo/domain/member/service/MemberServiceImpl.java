package com.friendzoo.domain.member.service;


import com.friendzoo.domain.member.dto.JoinRequestDTO;
import com.friendzoo.domain.member.dto.MemberTestDTO;
import com.friendzoo.domain.member.entity.Member;
import com.friendzoo.domain.member.repository.MemberRepository;
import com.friendzoo.props.JwtProps;
import com.friendzoo.security.MemberDTO;
import com.friendzoo.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final JWTUtil jwtUtil;
    private final JwtProps jwtProps;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    @Override
    public MemberTestDTO findUser(String email) {
        Member member = getMember(email);
        return this.entityToDto(member);
    }

    @Override
    public void join(JoinRequestDTO request) {
        memberRepository.findByEmail(request.getEmail())
                .ifPresent(member -> {
                    throw new IllegalArgumentException("이미 존재하는 회원입니다!");
                });

        Member member = Member.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .build();

        member.addRole(request.getRole()); // 회원가입시, USER 권한을 부여

        memberRepository.save(member);
    }


    @Override
    public Map<String, Object> login(String email, String password) {
        Member member = getMember(email);

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("password not found");
        }

        MemberDTO memberDTO = new MemberDTO(member.getEmail(), member.getPassword(), member.getName(),
                member.getMemberRoleList().stream().map(Enum::name).toList());

        log.info("memberService login memberDTO: {}", memberDTO);

        Map<String, Object> claims = memberDTO.getClaims();

        String accessToken = jwtUtil.generateToken(claims, jwtProps.getAccessTokenExpirationPeriod());
        String refreshToken = jwtUtil.generateToken(claims, jwtProps.getRefreshTokenExpirationPeriod());

        claims.put("accessToken", accessToken);
        claims.put("refreshToken", refreshToken);

        return claims;
    }


    /**
     * email로 회원을 찾는다.
     * @param email 이메일
     * @return 회원
     */
    private Member getMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다."));
    }
}

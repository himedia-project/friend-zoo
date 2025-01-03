package com.friendzoo.api.domain.member.service;


import com.friendzoo.api.domain.heart.service.HeartService;
import com.friendzoo.api.domain.member.dto.JoinRequestDTO;
import com.friendzoo.api.domain.member.dto.MemberTestDTO;
import com.friendzoo.api.domain.member.entity.Member;
import com.friendzoo.api.domain.member.repository.MemberRepository;
import com.friendzoo.api.domain.product.dto.ProductDTO;
import com.friendzoo.api.props.JwtProps;
import com.friendzoo.api.security.MemberDTO;
import com.friendzoo.api.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        Member member = this.getMember(email);
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


    @Override
    public String makeTempPassword() {
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < 10; i++) {
            buffer.append((char) ((int) (Math.random() * 55) + 65));
        }
        return buffer.toString();
    }

    /**
     * Social Login 성공시 JWT 토큰 발급
     * @param memberDTO Social Login 성공한 회원 정보
     * @return JWT 토큰 정보가 같이 있는 유저정보 Map
     */

    @Override
    @NotNull
    public Map<String, Object> getSocialClaims(MemberDTO memberDTO) {
        Map<String, Object> claims = memberDTO.getClaims();
        String jwtAccessToken = jwtUtil.generateToken(claims, jwtProps.getAccessTokenExpirationPeriod());      // 15분
        String jwtRefreshToken = jwtUtil.generateToken(claims, jwtProps.getRefreshTokenExpirationPeriod());     // 1일

        claims.put("accessToken", jwtAccessToken);
        claims.put("refreshToken", jwtRefreshToken);
        return claims;
    }


    /**
     * email로 회원을 찾는다.
     * @param email 이메일
     * @return 회원
     */
    @Transactional(readOnly = true)
    @Override
    public Member getMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다."));
    }
}

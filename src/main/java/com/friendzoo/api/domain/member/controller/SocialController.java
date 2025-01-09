package com.friendzoo.api.domain.member.controller;

import com.friendzoo.api.domain.member.service.MemberService;
import com.friendzoo.api.domain.member.service.SocialService;
import com.friendzoo.api.props.JwtProps;
import com.friendzoo.api.security.MemberDTO;
import com.friendzoo.api.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SocialController {

    private final MemberService memberService;
    private final SocialService socialService;
    private final JwtProps jwtProps;


    // 카카오 access token 받기
    @GetMapping("/api/member/kakao/token")
    public String getKakaoAccessToken(String code) {
        log.info("getKakaoAccessToken code: {}", code);

        return socialService.getKakaoAccessToken(code);
    }

    // 카카오 로그인 -> 유저정보 받기 + JWT 토큰 발급, cookie에 set
    @GetMapping("/api/member/kakao")
    public ResponseEntity<MemberController.LoginResponseDTO> getMemberFromKakao(String socialAccessToken, HttpServletResponse response) {
        log.info("getMemberFromKakao socialAccessToken: {}", socialAccessToken);

        MemberDTO memberDTO = socialService.getKakaoMember(socialAccessToken);
        Map<String, Object> loginClaims = memberService.getSocialClaims(memberDTO);

        CookieUtil.setTokenCookie(response, "refreshToken", (String) loginClaims.get("refreshToken"), jwtProps.getRefreshTokenExpirationPeriod());

        MemberController.LoginResponseDTO loginResponseDTO = MemberController.LoginResponseDTO.builder()
                .email(loginClaims.get("email").toString())
                .name(loginClaims.get("name").toString())
                .roles((List<String>) loginClaims.get("roleNames"))
                .accessToken((String) loginClaims.get("accessToken"))
                .build();

        log.info("loginResponseDTO: {}", loginResponseDTO);
        // 로그인 성공시, socialAccessToken, email, name, roles 반환
        return ResponseEntity.ok(loginResponseDTO);

    }

    // 구글 access token 받기
    @GetMapping("/api/member/google/token")
    public String getGoogleAccessToken(String code) {
        log.info("getGoogleAccessToken code: {}", code);

        return socialService.getGoogleAccessToken(code);
    }

    // 구글 로그인 -> 유저정보 받기 + JWT 토큰 발급, cookie에 set
    @GetMapping("/api/member/google")
    public ResponseEntity<MemberController.LoginResponseDTO> getMemberFromGoogle(String accessToken, HttpServletResponse response) {

        log.info("getMemberFromGoogle accessToken: {}", accessToken);

        MemberDTO memberDTO = socialService.getGoogleMember(accessToken);
        Map<String, Object> loginClaims = memberService.getSocialClaims(memberDTO);

        CookieUtil.setTokenCookie(response, "refreshToken", (String) loginClaims.get("refreshToken"), jwtProps.getRefreshTokenExpirationPeriod());

        MemberController.LoginResponseDTO loginResponseDTO = MemberController.LoginResponseDTO.builder()
                .email(loginClaims.get("email").toString())
                .name(loginClaims.get("name").toString())
                .roles((List<String>) loginClaims.get("roleNames"))
                .accessToken((String) loginClaims.get("accessToken"))
                .build();

        log.info("loginResponseDTO: {}", loginResponseDTO);
        // 로그인 성공시, accessToken, email, name, roles 반환
        return ResponseEntity.ok(loginResponseDTO);

    }



    // 네이버 access token 받기
    @GetMapping("/api/member/naver/token")
    String getNaverAccessToken(String code, String state) {
        log.info("code: " + code);
        log.info("state: " + state);

        return socialService.getNaverAccessToken(code, state);
    }


    // 네이버 로그인 -> 유저정보 받기 + JWT 토큰 발급
    @GetMapping("/api/member/naver")
    public ResponseEntity<MemberController.LoginResponseDTO> getMemberFromNaver(String accessToken, HttpServletResponse response) {
        log.info("getMemberFromNaver accessToken: {}", accessToken);

        MemberDTO memberDTO = socialService.getNaverMember(accessToken);
        Map<String, Object> loginClaims = memberService.getSocialClaims(memberDTO);

        CookieUtil.setTokenCookie(response, "refreshToken", (String) loginClaims.get("refreshToken"), jwtProps.getRefreshTokenExpirationPeriod());

        MemberController.LoginResponseDTO loginResponseDTO = MemberController.LoginResponseDTO.builder()
                .email(loginClaims.get("email").toString())
                .name(loginClaims.get("name").toString())
                .roles((List<String>) loginClaims.get("roleNames"))
                .accessToken((String) loginClaims.get("accessToken"))
                .build();

        log.info("loginResponseDTO: {}", loginResponseDTO);
        // 로그인 성공시, accessToken, email, name, roles 반환
        return ResponseEntity.ok(loginResponseDTO);

    }



}

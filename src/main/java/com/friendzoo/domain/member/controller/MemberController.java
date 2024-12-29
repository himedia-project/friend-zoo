package com.friendzoo.domain.member.controller;


import com.friendzoo.domain.member.dto.JoinRequestDTO;
import com.friendzoo.domain.member.dto.LoginDTO;
import com.friendzoo.domain.member.dto.MemberTestDTO;
import com.friendzoo.domain.member.service.MemberService;
import com.friendzoo.props.JwtProps;
import com.friendzoo.util.CookieUtil;
import com.friendzoo.util.JWTUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {
    // user - test = 1: N
    private final MemberService memberService;
    private final JWTUtil jwtUtil;
    private final JwtProps jwtProps;


    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<?> join(@Valid @RequestBody JoinRequestDTO request) {
        log.info("join: {}", request);
        memberService.join(request);
        return ResponseEntity.ok().build();
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    static class LoginResponseDTO {
        private String email;
        private String name;
        private List<String> roles;
        private String accessToken;
        private String refreshToken;
    }

    // login security에서 가져옴
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        log.info("login: {}", loginDTO);
        Map<String, Object> loginClaims = memberService.login(loginDTO.getEmail(), loginDTO.getPassword());


        // 로그인 성공시 accessToken, refreshToken 발급
        String accessToken = jwtUtil.generateToken(loginClaims, jwtProps.getAccessTokenExpirationPeriod());
        String refreshToken = jwtUtil.generateToken(loginClaims, jwtProps.getRefreshTokenExpirationPeriod());
        // access token, refreshToken 을 쿠키에 담아서 전달 (1day)
        // 15분
        CookieUtil.setTokenCookie(response, "accessToken", accessToken, jwtProps.getAccessTokenExpirationPeriod()); // 15min
        CookieUtil.setTokenCookie(response, "refreshToken", refreshToken, jwtProps.getRefreshTokenExpirationPeriod()); // 1day

        LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder()
                .email(loginClaims.get("email").toString())
                .name(loginClaims.get("name").toString())
                .roles((List<String>) loginClaims.get("roleNames"))
                .accessToken(loginClaims.get("accessToken").toString())
                .build();
        log.info("loginResponseDTO: {}", loginResponseDTO);

        return ResponseEntity.ok(loginResponseDTO);
    }

    @GetMapping("/api/user")
    public MemberTestDTO user(@RequestParam String email) {
        return memberService.findUser(email);
    }
}

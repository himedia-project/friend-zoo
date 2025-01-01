package com.friendzoo.api.domain.member.controller;


import com.friendzoo.api.domain.member.dto.LoginDTO;
import com.friendzoo.api.domain.member.dto.MemberResDTO;
import com.friendzoo.api.domain.member.service.AdminMemberService;
import com.friendzoo.api.domain.member.service.MemberService;
import com.friendzoo.api.dto.PageRequestDTO;
import com.friendzoo.api.dto.PageResponseDTO;
import com.friendzoo.api.props.JwtProps;
import com.friendzoo.api.util.CookieUtil;
import com.friendzoo.api.util.JWTUtil;
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
@RequestMapping("/api/admin/member")
@RequiredArgsConstructor
public class AdminMemberController {
    // user - test = 1: N
    private final AdminMemberService adminMemberService;
    private final MemberService memberService;

    private final JWTUtil jwtUtil;
    private final JwtProps jwtProps;



    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    static class LoginResponseDTO {
        private String email;
        private String name;
        private List<String> roles;
        private String accessToken;
    }

    // login security에서 가져옴
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        log.info("login: {}", loginDTO);
        Map<String, Object> loginClaims = memberService.login(loginDTO.getEmail(), loginDTO.getPassword());

        // 로그인 성공시 accessToken, refreshToken 생성
        String refreshToken = jwtUtil.generateToken(loginClaims, jwtProps.getRefreshTokenExpirationPeriod());
        String accessToken = loginClaims.get("accessToken").toString();
        // TODO: user 로그인시, refreshToken token 테이블에 저장
//        tokenService.saveRefreshToken(accessToken, refreshToken, memberService.getMember(loginDTO.getEmail()));
        // refreshToken 쿠키로 클라이언트에게 전달
        CookieUtil.setTokenCookie(response, "refreshToken", refreshToken, jwtProps.getRefreshTokenExpirationPeriod()); // 1day

        LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder()
                .email(loginClaims.get("email").toString())
                .name(loginClaims.get("name").toString())
                .roles((List<String>) loginClaims.get("roleNames"))
                .accessToken(accessToken)
                .build();

        log.info("loginResponseDTO: {}", loginResponseDTO);
        // 로그인 성공시, accessToken, email, name, roles 반환
        return ResponseEntity.ok(loginResponseDTO);
    }


    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        log.info("logout");
        // accessToken은 react 내 redux 상태 지워서 없앰
        // 쿠키 삭제
        CookieUtil.removeTokenCookie(response, "refreshToken");

        return ResponseEntity.ok("logout success!");
    }


    // 회원 목록
    @GetMapping("/list")
    public ResponseEntity<PageResponseDTO<MemberResDTO>> user(PageRequestDTO requestDTO) {
        PageResponseDTO<MemberResDTO> dto = adminMemberService.getList(requestDTO);
        return ResponseEntity.ok(dto);
    }

    // 해당 회원 상세정보 TODO

}

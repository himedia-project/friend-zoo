package com.friendzoo.domain.member.controller;

import com.friendzoo.domain.member.dto.MemberDTO;
import com.friendzoo.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    // user - test = 1: N
    private final MemberService memberService;

    @GetMapping("/api/user")
    public MemberDTO user(@RequestParam String email) {
        return memberService.findUser(email);
    }
}

package com.dsg.demo241226.domain.user.controller;

import com.dsg.demo241226.domain.user.dto.UserDto;
import com.dsg.demo241226.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    // user - test = 1: N
    private final UserService userService;

    @GetMapping("/api/user")
    public UserDto user(@RequestParam String email) {
        return userService.findUser(email);
    }
}

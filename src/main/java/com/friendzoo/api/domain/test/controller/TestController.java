package com.friendzoo.api.domain.test.controller;

import com.friendzoo.api.domain.test.dto.TestResDTO;
import com.friendzoo.api.domain.test.service.TestService;
import com.friendzoo.api.dto.PageRequestDTO;
import com.friendzoo.api.dto.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class TestController {


    private final TestService testService;

    // page querydsl 로 조회
    @GetMapping("/list/page")
    public ResponseEntity<PageResponseDTO<TestResDTO>> listPage(
            PageRequestDTO requestDTO
    ) //
    {
        log.info("listPage...............requestDTO: " + requestDTO);
        PageResponseDTO<TestResDTO> list = testService.getPage(requestDTO);
        return ResponseEntity.ok(list); // HTTP 웹개발 -> statusCode 200, 201, 404, 500 , body, msg
    }

}

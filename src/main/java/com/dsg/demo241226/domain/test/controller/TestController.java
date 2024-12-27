package com.dsg.demo241226.domain.test.controller;

import com.dsg.demo241226.domain.test.dto.TestCreateReqDTO;
import com.dsg.demo241226.dto.PageRequestDTO;
import com.dsg.demo241226.dto.PageResponseDTO;
import com.dsg.demo241226.domain.test.dto.TestResDTO;
import com.dsg.demo241226.domain.test.service.TestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class TestController {


    private final TestService testService;

    // list 조회
    @GetMapping("/list")
    public ResponseEntity<Page<TestResDTO>> list(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 10)
            Pageable pageable,
            @RequestParam(required = false) String keyword // &keyword=검색어
    )
    {
        Page<TestResDTO> list = testService.getList(pageable, keyword);
        return ResponseEntity.ok(list); // HTTP 웹개발 -> statusCode 200, 201, 404, 500 , body, msg
    }

    // page querydsl 로 조회
    @GetMapping("/list/page")
    public ResponseEntity<PageResponseDTO<TestResDTO>> listPage(
            PageRequestDTO requestDTO
    )
    {
        log.info("listPage...............requestDTO: " + requestDTO);
        PageResponseDTO<TestResDTO> list = testService.getPage(requestDTO);
        return ResponseEntity.ok(list); // HTTP 웹개발 -> statusCode 200, 201, 404, 500 , body, msg
    }


    // 상세보기
    @GetMapping("/{id}")
    public ResponseEntity<TestResDTO> detail(@PathVariable Long id) {
        return ResponseEntity.ok(testService.getOne(id));
    }


    // 등록
    @PostMapping
    public Long register(@Valid @RequestBody TestCreateReqDTO testDTO) {
        return testService.register(testDTO);
    }

    // 수정
    @PutMapping("/{id}")
    public Long modify(@PathVariable Long id, @Valid @RequestBody TestCreateReqDTO testDTO) {
        return testService.modify(id, testDTO);
    }


    // 삭제
    @DeleteMapping("{id}")
    public void remove(@PathVariable Long id) {
        testService.remove(id);
    }

}

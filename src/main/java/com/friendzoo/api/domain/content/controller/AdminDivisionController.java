package com.friendzoo.api.domain.content.controller;

import com.friendzoo.api.domain.content.service.DivisionService;
import com.friendzoo.api.domain.product.dto.CategoryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin/content/category")
@RequiredArgsConstructor
public class AdminDivisionController {

    private final DivisionService divisionService;

    // 리스트조회, 콘텐츠 등록시, select option용
    @GetMapping("/list")
    public ResponseEntity<List<CategoryDTO>> list() {
        List<CategoryDTO> dto = divisionService.list();
        return ResponseEntity.ok(dto);
    }

    // 카테고리 등록
    @PostMapping
    public ResponseEntity<Long> register(CategoryDTO categoryDTO) {
        log.info("register: {}", categoryDTO);
        Long categoryId = divisionService.register(categoryDTO);
        return ResponseEntity.ok(categoryId);
    }

    // 카테고리 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> remove(@PathVariable Long id) {
        log.info("remove: divisionId {}", id);
        divisionService.remove(id);
        return ResponseEntity.ok("success remove");
    }


}

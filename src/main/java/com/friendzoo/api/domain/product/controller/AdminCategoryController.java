package com.friendzoo.api.domain.product.controller;

import com.friendzoo.api.domain.product.dto.CategoryDTO;
import com.friendzoo.api.domain.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin/product/category")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    // 리스트조회, 상품 등록시, select option용
    @GetMapping("/list")
    public ResponseEntity<List<CategoryDTO>> list() {
        List<CategoryDTO> dto = categoryService.list();
        return ResponseEntity.ok(dto);
    }


}

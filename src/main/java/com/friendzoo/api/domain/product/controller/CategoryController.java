package com.friendzoo.api.domain.product.controller;

import com.friendzoo.api.domain.product.dto.CategoryDTO;
import com.friendzoo.api.domain.product.dto.ProductDTO;
import com.friendzoo.api.domain.product.service.CategoryService;
import com.friendzoo.api.domain.product.service.ProductService;
import com.friendzoo.api.props.JwtProps;
import com.friendzoo.api.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final JWTUtil jwtUtil;
    private final JwtProps jwtProps;

    @GetMapping("/list")
    public ResponseEntity<List<CategoryDTO>> categorylist() {
        List<CategoryDTO> dtoLists = categoryService.getCategory();
        return ResponseEntity.ok(dtoLists);
    }
}

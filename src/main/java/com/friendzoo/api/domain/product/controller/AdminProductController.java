package com.friendzoo.api.domain.product.controller;

import com.friendzoo.api.domain.product.dto.ProductDTO;
import com.friendzoo.api.domain.product.service.ProductAdminService;
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
@RequestMapping("/api/admin/product")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductAdminService productService;

    // 리스트조회
    @GetMapping("/list")
    public ResponseEntity<PageResponseDTO<ProductDTO>> list(PageRequestDTO requestDTO) {
        log.info("list: {}", requestDTO);
        PageResponseDTO<ProductDTO> dto = productService.list(requestDTO);
        return ResponseEntity.ok(dto);
    }

    // 상세조회


    // 드록


    // 수정


    // 삭제
}

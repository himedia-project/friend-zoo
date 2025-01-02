package com.friendzoo.api.domain.product.controller;

import com.friendzoo.api.domain.product.dto.ProductDTO;
import com.friendzoo.api.domain.product.service.AdminProductService;
import com.friendzoo.api.dto.PageRequestDTO;
import com.friendzoo.api.dto.PageResponseDTO;
import com.friendzoo.api.util.file.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/admin/product")
@RequiredArgsConstructor
public class AdminProductController {

    private final CustomFileUtil fileUtil;
    private final AdminProductService productService;

    // 리스트조회
    @GetMapping("/list")
    public ResponseEntity<PageResponseDTO<ProductDTO>> list(PageRequestDTO requestDTO) {
        log.info("list: {}", requestDTO);
        PageResponseDTO<ProductDTO> dto = productService.list(requestDTO);
        return ResponseEntity.ok(dto);
    }

    // 상세조회
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> read(Long id) {
        log.info("read: {}", id);
        ProductDTO dto = productService.getOne(id);
        return ResponseEntity.ok(dto);
    }


    // 등록
    @PostMapping
    public ResponseEntity<Long> register(ProductDTO productDTO) {
        log.info("register: {}", productDTO);
        Long id = productService.register(productDTO);
        return ResponseEntity.ok(id);
    }


    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<Long> modify(@PathVariable Long id, ProductDTO productDTO) {
        log.info("modify: {}, {}", id, productDTO);
        Long productId = productService.modify(id, productDTO);
        return ResponseEntity.ok(productId);
    }


    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> remove(Long id) {
        log.info("remove: {}", id);
        productService.remove(id);
        return ResponseEntity.ok("success");
    }


    // 이미지 불러오기
    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName) {
        return fileUtil.getFile(fileName);

    }
}

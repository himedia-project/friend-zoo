package com.friendzoo.api.domain.product.controller;

import com.friendzoo.api.domain.member.dto.MemberTestDTO;
import com.friendzoo.api.domain.member.entity.Member;
import com.friendzoo.api.domain.member.service.MemberService;
import com.friendzoo.api.domain.product.dto.CategoryDTO;
import com.friendzoo.api.domain.product.dto.ProductDTO;
import com.friendzoo.api.domain.product.entity.Product;
import com.friendzoo.api.domain.product.entity.ProductImageList;
import com.friendzoo.api.domain.product.service.ProductService;
import com.friendzoo.api.props.JwtProps;
import com.friendzoo.api.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final JWTUtil jwtUtil;
    private final JwtProps jwtProps;

//    @GetMapping("api/product/list")
//    public ProductDTO best(ProductDTO productDTO) {
//        return productService.getProduct(product);
//    }


    @GetMapping("/list")
    public ResponseEntity<List<ProductDTO>> bestSelect(ProductDTO productDTO) {
        List<ProductDTO> dtoLists = productService.getProducts(productDTO);
        return ResponseEntity.ok(dtoLists);
    }

    @GetMapping("/new")
    public ResponseEntity<List<ProductDTO>> newSelect(ProductDTO productDTO) {
        List<ProductDTO> dtoLists = productService.getNewProduct(productDTO);

        return ResponseEntity.ok(dtoLists);
    }
    @GetMapping("/list/{name}")
    public ResponseEntity<List<ProductDTO>> selectedlist(@PathVariable String name) {
        List<ProductDTO> dtoLists = productService.getSelectedCategory(name);
        return ResponseEntity.ok(dtoLists);
    }
    @GetMapping("/detail/{id}")
    public ResponseEntity<List<ProductDTO>> selectedItem(@PathVariable Long id) {
        List<ProductDTO> dtoLists = productService.getSelectedItem(id);
        return ResponseEntity.ok(dtoLists);
    }
    @GetMapping("/detail/category/{category_id}")
    public ResponseEntity<List<ProductDTO>> selectedCategoryItem(@PathVariable Long category_id) {
        List<ProductDTO> dtoLists = productService.getSelectedCategoryItem(category_id);
        return ResponseEntity.ok(dtoLists);
    }





}

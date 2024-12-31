package com.friendzoo.api.domain.product.controller;

import com.friendzoo.api.domain.member.dto.MemberTestDTO;
import com.friendzoo.api.domain.member.entity.Member;
import com.friendzoo.api.domain.member.service.MemberService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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





}

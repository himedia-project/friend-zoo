package com.friendzoo.api.domain.product.controller;

import com.friendzoo.api.domain.member.dto.MemberTestDTO;
import com.friendzoo.api.domain.member.entity.Member;
import com.friendzoo.api.domain.member.service.MemberService;
import com.friendzoo.api.domain.product.dto.ProductDTO;
import com.friendzoo.api.domain.product.entity.Product;
import com.friendzoo.api.domain.product.service.ProductService;
import com.friendzoo.api.props.JwtProps;
import com.friendzoo.api.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public ResponseEntity<List<Product>> bestSelect(ProductDTO productDTO) {
        String best = productDTO.getBest();

        return ResponseEntity.ok(productService.getProducts(best));
    }



}

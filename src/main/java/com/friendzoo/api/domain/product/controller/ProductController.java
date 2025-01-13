package com.friendzoo.api.domain.product.controller;

import com.friendzoo.api.domain.member.dto.MemberTestDTO;
import com.friendzoo.api.domain.member.entity.Member;
import com.friendzoo.api.domain.member.service.MemberService;
import com.friendzoo.api.domain.product.converter.StringToProductBestConverter;
import com.friendzoo.api.domain.product.dto.CategoryDTO;
import com.friendzoo.api.domain.product.dto.ProductDTO;
import com.friendzoo.api.domain.product.entity.Product;
import com.friendzoo.api.domain.product.enums.ProductBest;
import com.friendzoo.api.domain.product.service.ProductService;
import com.friendzoo.api.props.JwtProps;
import com.friendzoo.api.security.MemberDTO;
import com.friendzoo.api.util.JWTUtil;
import com.friendzoo.api.util.file.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.InitBinder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final CustomFileUtil fileUtil;
    private final ProductService productService;
    private final JWTUtil jwtUtil;
    private final JwtProps jwtProps;

//    @GetMapping("api/product/list")
//    public ProductDTO best(ProductDTO productDTO) {
//        return productService.getProduct(product);
//    }

//    @InitBinder
//    public void initBinder(WebDataBinder binder){
//        binder.registerCustomEditor(ProductBest.class, new StringToProductBestConverter());
//    }

    @GetMapping("/list/best")
    public ResponseEntity<List<ProductDTO>> bestSelect(ProductDTO productDTO) {
        List<ProductDTO> dtoLists = productService.getBestProducts(productDTO);
        return ResponseEntity.ok(dtoLists);
    }

    @GetMapping("/list/mdPick")
    public ResponseEntity<List<ProductDTO>> mdPickSelect(ProductDTO productDTO) {
        List<ProductDTO> dtoLists = productService.getMdPickProducts(productDTO);
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

    // 리스트 조회
    @GetMapping("/list/all")
    public ResponseEntity<List<ProductDTO>> allList(
            @AuthenticationPrincipal MemberDTO memberDTO,
            ProductDTO productDTO
    ) {
        log.info("allList memberDTO: {}, productDTO: {}", memberDTO, productDTO);
        String email = "";
        if (memberDTO != null) {
            email = memberDTO.getEmail();
        }

        List<ProductDTO> dtoLists = productService.getAllList(email, productDTO);
        return ResponseEntity.ok(dtoLists);
    }


    @GetMapping("/detail/{productId}")
    public ResponseEntity<ProductDTO> selectedItem(@AuthenticationPrincipal MemberDTO memberDTO, @PathVariable Long productId) {
        log.info("detail selectedItem memberDTO: {}, productId: {}", memberDTO, productId);
        String email = "";
        if (memberDTO != null) {
            email = memberDTO.getEmail();
        }
        ProductDTO selectedItem = productService.getSelectedItem(email, productId);

        return ResponseEntity.ok(selectedItem);
    }

    @GetMapping("/detail/category/{categoryId}")
    public ResponseEntity<List<ProductDTO>> selectedCategoryItem(@AuthenticationPrincipal MemberDTO memberDTO, @PathVariable Long categoryId) {
        log.info("detail category memberDTO: {}, categoryId: {}", memberDTO, categoryId);
        String email = "";
        if (memberDTO != null) {
            email = memberDTO.getEmail();
        }
        List<ProductDTO> dtoLists = productService.getSelectedCategoryItem(email, categoryId);
        return ResponseEntity.ok(dtoLists);
    }


    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName) {
        return fileUtil.getFile(fileName);
    }


}

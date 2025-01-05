package com.friendzoo.api.domain.heart.controller;

import com.friendzoo.api.domain.content.dto.ContentDTO;
import com.friendzoo.api.domain.heart.service.HeartService;
import com.friendzoo.api.domain.product.dto.ProductDTO;
import com.friendzoo.api.security.MemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/heart")
@RequiredArgsConstructor
public class HeartController {
    private final HeartService heartService;

//    @PostMapping("/detail/{email}/{id}")
//    public ResponseEntity<List<HeartDTO>> heartItemCheck(@PathVariable String email
//            , @PathVariable Long id) {
//        List<HeartDTO> dtoLists = heartService.getHeartItemCheck(email,id);
//        return null;
//    }

    // 찜하기/찜하기 취소 상품
    @PostMapping("/product/{productId}")
    public ResponseEntity<?> heartProduct(@PathVariable Long productId, @AuthenticationPrincipal MemberDTO memberDTO) {
        heartService.heartProduct(productId, memberDTO.getEmail());
        return ResponseEntity.ok().build();
    }

    // 찜하기/찜하기 취소 콘텐츠
    @PostMapping("/content/{contentId}")
    public ResponseEntity<?> heartContent(@PathVariable Long contentId, @AuthenticationPrincipal MemberDTO memberDTO) {
        heartService.heartContent(contentId, memberDTO.getEmail());
        return ResponseEntity.ok().build();
    }

    // 해당유저의 찜목록(상품)
    @GetMapping("/product/list")
    public List<ProductDTO> heartProductList(@AuthenticationPrincipal MemberDTO memberDTO) {
        return heartService.findProductListByMember(memberDTO.getEmail());
    }

    // 해당유저의 찜목록(콘텐츠)
    @GetMapping("/content/list")
    public List<ContentDTO> heartContentList(@AuthenticationPrincipal MemberDTO memberDTO) {
        return heartService.findContentListByMember(memberDTO.getEmail());
    }


}

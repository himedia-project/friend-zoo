package com.friendzoo.api.domain.content.controller;

import com.friendzoo.api.domain.content.dto.ContentDTO;
import com.friendzoo.api.domain.content.service.ContentService;
import com.friendzoo.api.domain.content.service.ContentServiceImpl;
import com.friendzoo.api.domain.product.dto.ProductDTO;
import com.friendzoo.api.dto.PageRequestDTO;
import com.friendzoo.api.dto.PageResponseDTO;
import com.friendzoo.api.security.MemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
public class ContentController {
    private final ContentService contentService;

    @GetMapping("/list")
    public ResponseEntity<List<ContentDTO>> getList(@AuthenticationPrincipal MemberDTO memberDTO) {
        List<ContentDTO> dto = contentService.findListBy(memberDTO.getEmail());

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/detail/{content_id}")
    public ResponseEntity<List<ContentDTO>> getDetailList(@AuthenticationPrincipal MemberDTO memberDTO,@PathVariable Long content_id) {
        List<ContentDTO> dto = contentService.findDetailListBy(memberDTO.getEmail(),content_id);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/search/tags/{tag_id}")
    public ResponseEntity<List<ContentDTO>> getTagList(@AuthenticationPrincipal MemberDTO memberDTO,@PathVariable Long tag_id) {
        List<ContentDTO> dto = contentService.findTagsItem(memberDTO.getEmail(),tag_id);

        return ResponseEntity.ok(dto);
    }


}

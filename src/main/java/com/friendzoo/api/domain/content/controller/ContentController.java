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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
public class ContentController {
    private final ContentService contentService;

//    @GetMapping("/list")
//    public ResponseEntity<List<ContentDTO>> getContent(ContentDTO contentDTO) {
//        List<ContentDTO> dtoLists = contentService.getContent(contentDTO);
//        return ResponseEntity.ok(dtoLists);
//    }
    @GetMapping("/list")
    public ResponseEntity<PageResponseDTO<ContentDTO>> getList(PageRequestDTO requestDTO, @AuthenticationPrincipal MemberDTO memberDTO) {

        log.info("list..............." + requestDTO);
        PageResponseDTO<ContentDTO> dto = contentService.findListBy(requestDTO,memberDTO.getEmail());

        return ResponseEntity.ok(dto);
    }

}

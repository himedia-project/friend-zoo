package com.friendzoo.api.domain.content.controller;


import com.friendzoo.api.domain.content.dto.ContentDTO;
import com.friendzoo.api.domain.content.service.AdminContentService;
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
@RequestMapping("/api/admin/content")
@RequiredArgsConstructor
public class AdminContentController {

    private final AdminContentService contentService;

    @GetMapping("/list")
    public ResponseEntity<?> list(PageRequestDTO requestDTO) {

        log.info("list..............." + requestDTO);
        PageResponseDTO<ContentDTO> dto = contentService.getList(requestDTO);

        return ResponseEntity.ok(dto);
    }
}

package com.friendzoo.api.domain.content.controller;


import com.friendzoo.api.domain.content.dto.ContentDTO;
import com.friendzoo.api.domain.content.service.AdminContentService;
import com.friendzoo.api.dto.PageRequestDTO;
import com.friendzoo.api.dto.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/admin/content")
@RequiredArgsConstructor
public class AdminContentController {

    private final AdminContentService contentService;

    @GetMapping("/list")
    public ResponseEntity<PageResponseDTO<ContentDTO>> list(PageRequestDTO requestDTO) {

        log.info("list..............." + requestDTO);
        PageResponseDTO<ContentDTO> dto = contentService.getList(requestDTO);

        return ResponseEntity.ok(dto);
    }

    // 상세조회
    @GetMapping("/{id}")
    public ResponseEntity<?> read(Long id) {
        log.info("read..............." + id);
        ContentDTO dto = contentService.getOne(id);
        return ResponseEntity.ok(dto);
    }

    // 등록
    @PostMapping
    public ResponseEntity<Long> register(ContentDTO dto) {
        log.info("register..............." + dto);
        Long id = contentService.register(dto);
        return ResponseEntity.ok(id);
    }


    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<Long> modify(@PathVariable Long id, ContentDTO dto) {
        log.info("modify: {}, {}", id, dto);
        Long contentId = contentService.modify(id, dto);
        return ResponseEntity.ok(contentId);
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> remove(@PathVariable Long id) {
        log.info("remove..............." + id);
        contentService.remove(id);
        return ResponseEntity.ok("success");
    }
}

package com.friendzoo.api.domain.content.service;

import com.friendzoo.api.domain.content.dto.ContentDTO;
import com.friendzoo.api.domain.content.entity.Content;
import com.friendzoo.api.domain.content.repository.ContentRepository;
import com.friendzoo.api.dto.PageRequestDTO;
import com.friendzoo.api.dto.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class AdminContentServiceImpl implements AdminContentService{

    private final ContentRepository contentRepository;

    @Transactional(readOnly = true)
    @Override
    public PageResponseDTO<ContentDTO> getList(PageRequestDTO requestDTO) {
        log.info("getList..............");

        Page<Content> result = contentRepository.findListBy(requestDTO);
        return PageResponseDTO.<ContentDTO>withAll()
                .dtoList(result.stream().map(this::entityToDTO).toList())
                .totalCount(result.getTotalElements())
                .pageRequestDTO(requestDTO)
                .build();
    }
}

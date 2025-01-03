package com.friendzoo.api.domain.content.excel;

import com.friendzoo.api.domain.content.dto.ContentDTO;
import com.friendzoo.api.domain.content.service.AdminContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContentCreator {


    private final AdminContentService contentService;

    public void create(ContentDTO dto) {

        contentService.register(dto);

    }

}

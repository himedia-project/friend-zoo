package com.friendzoo.api.domain.content.service;

import com.friendzoo.api.domain.content.dto.ContentDTO;
import com.friendzoo.api.dto.PageRequestDTO;
import com.friendzoo.api.dto.PageResponseDTO;

public interface AdminContentService {

    PageResponseDTO<ContentDTO> getList(PageRequestDTO requestDTO);
}

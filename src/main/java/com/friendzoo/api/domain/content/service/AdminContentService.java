package com.friendzoo.api.domain.content.service;

import com.friendzoo.api.domain.content.dto.ContentDTO;
import com.friendzoo.api.domain.content.entity.Content;
import com.friendzoo.api.domain.content.entity.ContentImage;
import com.friendzoo.api.dto.PageRequestDTO;
import com.friendzoo.api.dto.PageResponseDTO;

import java.util.List;

public interface AdminContentService {

    PageResponseDTO<ContentDTO> getList(PageRequestDTO requestDTO);

    default ContentDTO entityToDTO(Content content) {
        ContentDTO contentDTO = ContentDTO.builder()
                .id(content.getId())
                .title(content.getTitle())
                .body(content.getBody())
                .createdAt(content.getCreatedAt())
                .modifiedAt(content.getModifiedAt())
                .build();

        List<ContentImage> imageList = content.getImageList();

        if (imageList == null || imageList.isEmpty()) {
            return contentDTO;
        }

        List<String> fileNameList = imageList.stream().map(ContentImage::getImageName).toList();

        contentDTO.setUploadFileNames(fileNameList);
        contentDTO.setDivisionId(content.getDivision().getId());
        contentDTO.setDivisionName(content.getDivision().getName());

        return contentDTO;
    }
}

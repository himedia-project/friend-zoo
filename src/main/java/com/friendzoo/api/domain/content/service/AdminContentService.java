package com.friendzoo.api.domain.content.service;

import com.friendzoo.api.domain.content.dto.ContentDTO;
import com.friendzoo.api.domain.content.entity.Content;
import com.friendzoo.api.domain.content.entity.ContentImage;
import com.friendzoo.api.domain.content.entity.Division;
import com.friendzoo.api.dto.PageRequestDTO;
import com.friendzoo.api.dto.PageResponseDTO;

import java.util.List;

public interface AdminContentService {

    PageResponseDTO<ContentDTO> getList(PageRequestDTO requestDTO);

    ContentDTO getOne(Long id);

    Long register(ContentDTO dto);

    void remove(Long id);

    Long modify(Long id, ContentDTO dto);

    /**
     * Content -> ContentDTO 변환
     * @param content Content
     * @return ContentDTO
     */
    default ContentDTO entityToDTO(Content content) {
        ContentDTO contentDTO = ContentDTO.builder()
                .id(content.getId())
                .title(content.getTitle())
                .body(content.getBody())
                .tags(content.getContentTagList().stream().map(tag -> tag.getTag().getName()).toList())
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

    /**
     * ContentDTO -> Content 변환
     * @param dto ContentDTO
     * @param division Division
     * @return Content
     */
    default Content dtoToEntity(ContentDTO dto, Division division) {

        Content content = Content.builder()
                .title(dto.getTitle())
                .body(dto.getBody())
                .division(division)
                .delFlag(false)
                .build();
        // 업로드 처리가 끝난 파일들의 이름 리스트
        List<String> uploadFileNames = dto.getUploadFileNames();

        if (uploadFileNames == null) {
            return content;
        }

        uploadFileNames.forEach(content::addImageString);

        return content;
    }
}

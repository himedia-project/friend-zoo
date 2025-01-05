package com.friendzoo.api.domain.content.service;

import com.friendzoo.api.domain.content.dto.ContentDTO;
import com.friendzoo.api.domain.content.entity.Content;
import com.friendzoo.api.domain.content.entity.ContentImage;
import com.friendzoo.api.domain.content.repository.ContentRepository;
import com.friendzoo.api.domain.heart.repository.HeartRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {
    private final ContentRepository contentRepository;
    private final JPAQueryFactory queryFactory;
    private final HeartRepository heartRepository;

//    @Override
    public List<ContentDTO> getContent(ContentDTO contentDTO) {
//                List<Content> dtoLists = contentRepository.findBestProducts(Sort.by(Sort.Direction.DESC,"createdAt"));
//                List<ContentDTO> dtoList = dtoLists.stream()
//                        .map(this::entityToDTO)
//                        .collect(Collectors.toList());
//                return dtoList;
        return null;
    }

    @Override
    public List<ContentDTO> findListBy(String email) {
        List<Content> result = contentRepository.findListByEmail(email);
        // heartService email
              List<ContentDTO> dtoResult =  result.stream().map(content -> {
                       // isHeart 여부 <- content, email
                    boolean isHeart = heartRepository.findExistedHeartContent(email,content.getId());
                    ContentDTO dto = ContentDTO.builder()
                            .id(content.getId())
                            .divisionName(content.getDivision().getName())
                            .title(content.getTitle())
                            .uploadFileNames(content.getImageList().stream().map(ContentImage::getImageName).toList())
                            .heartCount((long) content.getHeartList().size())
                            .isHeart(isHeart)
                            .createdAt(content.getCreatedAt())
                            .modifiedAt(content.getModifiedAt())
                            .build();

                 return dto;
                }).toList();
        return dtoResult;
    }

    @Override
    public List<ContentDTO> findDetailListBy(String email,Long content_id) {
        List<Content> result = contentRepository.findDetailListBy(email,content_id);
        // heartService email
        List<ContentDTO> dtoResult =  result.stream().map(content -> {
            // isHeart 여부 <- content, email
            boolean isHeart = heartRepository.findExistedHeartContent(email,content.getId());
//            List<Content> getTags = contentRepository.findDetailTagList(content_id);
            ContentDTO dto = ContentDTO.builder()
                    .id(content.getId())
                    .divisionId(content.getDivision().getId())
                    .divisionName(content.getDivision().getName())
                    .title(content.getTitle())
                    .uploadFileNames(content.getImageList().stream().map(ContentImage::getImageName).toList())
                    .heartCount((long) content.getHeartList().size())
                    .isHeart(isHeart)
                    .createdAt(content.getCreatedAt())
                    .modifiedAt(content.getModifiedAt())
                    .tags(content.getContentTagList().stream().map(contentTag -> contentTag.getTag().getName()).toList())
//                    .tags()
                    .build();

            return dto;
        }).toList();
        return dtoResult;
    }

    @Override
    public List<ContentDTO> findTagsItem(String email,Long tag_id) {
        List<Content> result = contentRepository.findTagsItem(email,tag_id);
        // heartService email
        List<ContentDTO> dtoResult =  result.stream().map(content -> {
            // isHeart 여부 <- content, email
            boolean isHeart = heartRepository.findExistedHeartContent(email,content.getId());
            ContentDTO dto = ContentDTO.builder()
                    .id(content.getId())
                    .divisionId(content.getDivision().getId())
                    .divisionName(content.getDivision().getName())
                    .title(content.getTitle())
                    .uploadFileNames(content.getImageList().stream().map(ContentImage::getImageName).toList())
                    .heartCount((long) content.getHeartList().size())
                    .isHeart(isHeart)
                    .createdAt(content.getCreatedAt())
                    .modifiedAt(content.getModifiedAt())
                    //.tags(content.getContentTagList().stream().map(contentTag -> contentTag.getTag().getName()).toList())
//                    .tags()
                    .build();

            return dto;
        }).toList();
        return dtoResult;
    }

//    @Override
//    public boolean getList(PageRequestDTO requestDTO, String email) {
//        boolean return_bool = false;
//        List<Content> result = contentRepository.findListBy(requestDTO,email);
//        if(!result.isEmpty())
//        {
//            return_bool = true;
//        }
//        return return_bool;
//    }

//    @Transactional(readOnly = true)
//    @Override
//    public PageResponseDTO<ContentDTO> getList(PageRequestDTO requestDTO) {
//        log.info("getList..............");
//
//        Page<Content> result = contentRepository.findListBy(requestDTO);
//        return PageResponseDTO.<ContentDTO>withAll()
//                .dtoList(result.stream().map(this::entityToDTO).toList())
//                .totalCount(result.getTotalElements())
//                .pageRequestDTO(requestDTO)
//                .build();
//    }


    @Transactional(readOnly = true)
    @Override
    public Content getEntity(Long contentId) {
        return contentRepository.findById(contentId)
                .orElseThrow(() -> new EntityNotFoundException("해당 컨텐츠가 존재하지 않습니다. contentId: " + contentId));
    }

}

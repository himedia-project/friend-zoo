package com.friendzoo.api.domain.content.service;

import com.friendzoo.api.domain.content.dto.ContentDTO;
import com.friendzoo.api.domain.content.entity.Content;
import com.friendzoo.api.domain.content.entity.ContentImage;
import com.friendzoo.api.domain.content.entity.ContentTag;
import com.friendzoo.api.domain.content.entity.Tag;
import com.friendzoo.api.domain.content.repository.ContentRepository;
import com.friendzoo.api.domain.heart.repository.HeartRepository;
import com.friendzoo.api.domain.product.dto.ProductDTO;
import com.friendzoo.api.domain.product.entity.Product;
import com.friendzoo.api.domain.product.entity.ProductImage;
import com.friendzoo.api.dto.PageRequestDTO;
import com.friendzoo.api.dto.PageResponseDTO;
import com.querydsl.core.QueryFactory;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.friendzoo.api.domain.content.entity.QContent.content;
import static com.friendzoo.api.domain.content.entity.QContentImage.contentImage;
import static com.friendzoo.api.domain.heart.entity.QHeart.heart;

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
                    boolean isHeart = heartRepository.findHeartContent(email,content.getId());
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
            boolean isHeart = heartRepository.findHeartContent(email,content.getId());
            List<Content> getTags = contentRepository.findDetailTagList(content_id);
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

}

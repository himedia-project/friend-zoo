package com.friendzoo.api.domain.content.service;



import com.friendzoo.api.domain.content.dto.ContentDTO;
import com.friendzoo.api.domain.content.entity.Content;
import com.friendzoo.api.domain.product.dto.ProductDTO;
import com.friendzoo.api.domain.product.entity.Product;
import com.friendzoo.api.dto.PageRequestDTO;
import com.friendzoo.api.dto.PageResponseDTO;

import java.util.List;

public interface ContentService {
    List<ContentDTO> getContent(ContentDTO contentDTO);

    PageResponseDTO<ContentDTO> findListBy(PageRequestDTO requestDTO,String email);

//    default ContentDTO entityToDTO(Content content , boolean isHeart) {
//        if (content == null) {
//            return null;
//        }
//
//        return ContentDTO.builder()
//                .id(content.getId())
//                .divisionName(content.getDivision().getName())
//                .title(content.getTitle())
//                .heartCount((long) content.getHeartList().size())
//                  .isHeart(isHeart)
//
//
////                .tags(content.getContentTagList().stream().toList())
////                .best(content.getBest())
////                .mdPick(content.getMdPick())
////                .discountPrice(content.getDiscountPrice())
//                .build();
//    }
}

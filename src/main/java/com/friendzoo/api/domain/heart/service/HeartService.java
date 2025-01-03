package com.friendzoo.api.domain.heart.service;

import com.friendzoo.api.domain.heart.dto.HeartDTO;
import com.friendzoo.api.domain.heart.entity.Heart;
import com.friendzoo.api.domain.product.dto.ProductDTO;

import java.util.List;
public interface HeartService {

//    List<HeartDTO> getHeartItemCheck(String email, Long id);

    default HeartDTO entityToDTO(Heart heart) {
        if (heart == null) {
            return null;
        }

        return HeartDTO.builder()
                .heartId(heart.getId())
                .email(heart.getMember().getEmail())
                .productId(heart.getProduct().getId())
                .contentId(null)
                .build();
    }

    /**
     * 회원의 찜 상품 리스트 조회
     * @param email 회원 이메일
     * @return 찜 상품 리스트
     */
    List<ProductDTO> findProductListByMember(String email);

    /**
     * 상품 찜하기/찜 취소
     * @param productId 상품 ID
     * @param email 회원 이메일
     */
    void heartProduct(Long productId, String email);

    /**
     * 컨텐츠 찜하기/찜 취소
     * @param contentId 컨텐츠 ID
     * @param email 회원 이메일
     */
    void heartContent(Long contentId, String email);
}

package com.friendzoo.api.domain.heart.service;

import com.friendzoo.api.domain.heart.dto.HeartDTO;
import com.friendzoo.api.domain.heart.entity.Heart;

import java.util.List;
public interface HeartService {

    List<HeartDTO> getHeartItemCheck(String email, Long id);

    default HeartDTO entityToDTO(Heart heart) {
        if (heart == null) {
            return null;
        }

        return HeartDTO.builder()
                .heartId(heart.getId())
                .email(heart.getEmail())
                .productId(heart.getProductId())
                .contentId(null)
                .build();
    }

}

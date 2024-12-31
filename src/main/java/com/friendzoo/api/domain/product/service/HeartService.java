package com.friendzoo.api.domain.product.service;

import com.friendzoo.api.domain.product.dto.HeartDTO;
import com.friendzoo.api.domain.product.dto.ProductDTO;
import com.friendzoo.api.domain.product.entity.Heart;
import com.friendzoo.api.domain.product.entity.Product;
import org.springframework.stereotype.Service;

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

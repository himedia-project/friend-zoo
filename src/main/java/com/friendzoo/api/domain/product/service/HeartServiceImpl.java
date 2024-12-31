package com.friendzoo.api.domain.product.service;

import com.friendzoo.api.domain.product.dto.HeartDTO;
import com.friendzoo.api.domain.product.dto.ProductDTO;
import com.friendzoo.api.domain.product.entity.Heart;
import com.friendzoo.api.domain.product.repository.HeartRepository;
import com.friendzoo.api.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class HeartServiceImpl implements HeartService{
    private final HeartRepository heartRepository;


    @Override
    public List<HeartDTO> getHeartItemCheck(String email, Long id) {
        List<Heart> dtoLists = heartRepository.findrelatedItem(email,id);
        List<HeartDTO> result = new ArrayList<>();
        if(!dtoLists.isEmpty()) {
            if (!dtoLists.isEmpty()) {
                heartRepository.deleteById(dtoLists.get(0).getId());
                result  = dtoLists.stream()
                        .map(this::entityToDTO) // Product를 ProductDTO로 변환
                        .collect(Collectors.toList()); // 리스트로 수집
                return result;
            }

        }
        else {
            Long product_id = id;
            String email_address = email;

            Heart heart = new Heart();
            heart.setProductId(product_id);
            heart.setEmail(email_address);
            heart.setContentId(null);
            Heart heart_entity = heartRepository.save(heart);
            result = List.of(entityToDTO(heart_entity));
            return result;
        }

        return result;
    }
}

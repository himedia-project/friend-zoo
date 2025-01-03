package com.friendzoo.api.domain.content.service;

import com.friendzoo.api.domain.content.entity.Division;
import com.friendzoo.api.domain.product.dto.CategoryDTO;

import java.util.List;

public interface DivisionService {

    List<CategoryDTO> list();
    Long register(CategoryDTO categoryDTO);
    void remove(Long divisionId);

    default CategoryDTO entityToDTO(Division division) {
        return CategoryDTO.builder()
                .categoryId(division.getId())
                .name(division.getName())
                .logo(division.getLogo())
                .build();
    }

    default Division dtoToEntity(CategoryDTO dto) {
        return Division.builder()
                .id(dto.getCategoryId())
                .name(dto.getName())
                .logo(dto.getLogo())
                .build();
    }

}

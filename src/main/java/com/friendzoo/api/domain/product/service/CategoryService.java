package com.friendzoo.api.domain.product.service;

import com.friendzoo.api.domain.product.dto.CategoryDTO;
import com.friendzoo.api.domain.product.entity.Category;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getCategory();
//    List<CategoryDTO> getSelectedCategory(Long id);
    default CategoryDTO entityToDTO(Category category) {
        if (category == null) {
            return null;
        }

        return CategoryDTO.builder()
                .categoryId(category.getId())
                .name(category.getName())
                .logo(category.getLogo())
                .build();
    }
}

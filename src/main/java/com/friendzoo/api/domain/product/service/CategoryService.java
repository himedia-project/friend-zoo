package com.friendzoo.api.domain.product.service;

import com.friendzoo.api.domain.product.dto.CategoryDTO;
import com.friendzoo.api.domain.product.dto.ProductDTO;
import com.friendzoo.api.domain.product.entity.Category;
import com.friendzoo.api.domain.product.entity.Product;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getCategory();
//    List<CategoryDTO> getSelectedCategory(Long id);

    List<CategoryDTO> list();

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

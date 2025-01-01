package com.friendzoo.api.domain.product.service;

import com.friendzoo.api.domain.product.dto.CategoryDTO;
import com.friendzoo.api.domain.product.dto.ProductDTO;
import com.friendzoo.api.domain.product.entity.Category;
import com.friendzoo.api.domain.product.entity.Product;
import com.friendzoo.api.domain.product.repository.CategoryRepository;
import com.friendzoo.api.domain.product.repository.ProductRepository;
import com.friendzoo.api.props.JwtProps;
import com.friendzoo.api.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final JWTUtil jwtUtil;
    private final JwtProps jwtProps;
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDTO> getCategory() {
        List<Category> dtoLists = categoryRepository.findCategory();
        List<CategoryDTO> dtoList = dtoLists.stream()
                .map(this::entityToDTO) // Product를 ProductDTO로 변환
                .collect(Collectors.toList()); // 리스트로 수집
        return dtoList;
    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoryDTO> list() {
        return categoryRepository.findAll().stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

//    @Override
//    public List<CategoryDTO> getSelectedCategory(Long id) {
//        List<Category> dtoLists = categoryRepository.findSelectedCategory(id);
//        List<CategoryDTO> dtoList = dtoLists.stream()
//                .map(this::entityToDTO) // Product를 ProductDTO로 변환
//                .collect(Collectors.toList()); // 리스트로 수집
//        return dtoList;
//    }
}

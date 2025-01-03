package com.friendzoo.api.domain.product.service;

import com.friendzoo.api.domain.product.dto.CategoryDTO;
import com.friendzoo.api.domain.product.entity.Category;
import com.friendzoo.api.domain.product.repository.CategoryRepository;
import com.friendzoo.api.domain.product.repository.ProductRepository;
import com.friendzoo.api.util.file.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminCategoryServiceImpl implements AdminCategoryService {

    private final CategoryRepository categoryRepository;
    private final CustomFileUtil fileUtil;

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    @Override
    public List<CategoryDTO> list() {
        return categoryRepository.findAll().stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Long register(CategoryDTO dto) {
        // 파일 s3 업로드
        if (dto.getFile() != null || dto.getFile().isEmpty()) {
            MultipartFile file = dto.getFile();
            String uploadFileName = fileUtil.uploadS3File(file);
            dto.setFileName(uploadFileName);
        }
        Category result = categoryRepository.save(this.dtoToEntity(dto));
        return result.getId();
    }

    @Override
    public void remove(Long categoryId) {

        // 연관관계가 있는 카테고리라면 삭제 불가능
        if (productRepository.existsByCategoryId(categoryId)) {
            throw new IllegalStateException("연관된 상품이 있어 카테고리 삭제가 불가능합니다.");
        }

        categoryRepository.deleteById(categoryId);
    }
}
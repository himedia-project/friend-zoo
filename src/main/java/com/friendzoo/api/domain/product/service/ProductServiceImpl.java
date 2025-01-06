package com.friendzoo.api.domain.product.service;


import com.friendzoo.api.domain.product.entity.ProductImage;
import com.friendzoo.api.domain.heart.repository.HeartRepository;
import com.friendzoo.api.domain.product.dto.ProductDTO;
import com.friendzoo.api.domain.product.entity.Product;
import com.friendzoo.api.domain.product.repository.ProductRepository;
import com.friendzoo.api.props.JwtProps;
import com.friendzoo.api.util.JWTUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final HeartRepository heartRepository;

    @Override
    public List<ProductDTO> getBestProducts(ProductDTO productDTO) {
                List<Product> dtoLists = productRepository.findBestProducts();
                List<ProductDTO> dtoList = dtoLists.stream()
                        .map(this::entityToDTO) // Product를 ProductDTO로 변환
                        .collect(Collectors.toList()); // 리스트로 수집
                return dtoList;

    }
    @Override
    public List<ProductDTO> getMdPickProducts(ProductDTO productDTO) {
                List<Product> dtoLists = productRepository.findMdPickProducts();
                List<ProductDTO> dtoList = dtoLists.stream()
                        .map(this::entityToDTO) // Product를 ProductDTO로 변환
                        .collect(Collectors.toList()); // 리스트로 수집
                return dtoList;
            }

    public List<ProductDTO> getNewProduct(ProductDTO productDTO) {
//Sort.by(""); asc
        List<Product> dtoLists = productRepository.findNewProducts(Sort.by(Sort.Direction.DESC,"createdAt"));
        List<ProductDTO> dtoList = dtoLists.stream()
                .map(this::entityToDTO) // Product를 ProductDTO로 변환
                .toList(); // 리스트로 수집
        return dtoList;

    }
    @Override
    public ProductDTO getDTO(ProductDTO productDTO) {
        return null;
    }

    @Override
    public List<ProductDTO> getSelectedCategory(String name) {
        List<Product> dtoLists = productRepository.findSelectedCategory(name);
        List<ProductDTO> dtoList = dtoLists.stream()
                .map(this::entityToDTO) // Product를 ProductDTO로 변환
                .collect(Collectors.toList()); // 리스트로 수집
        return dtoList;
    }

    @Override
    public ProductDTO getSelectedItem(String email,Long productId) {
        Product product = productRepository.findDetailProduct(email,productId);
            // isHeart 여부 <- product, email
            boolean isHeart = heartRepository.findProductHeart(email,product.getId());
            ProductDTO dto = ProductDTO.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .category(product.getCategory() != null ? product.getCategory().getId() : null)
                    .price(product.getPrice())
                    .best(product.getBest())
                    .mdPick(product.getMdPick())
                    .uploadFileNames(product.getImageList().stream().map(ProductImage::getImageName).toList())
                    .isHeart(isHeart)
                    .description(product.getDescription())
                    .stockNumber(product.getStockNumber())
                    .categoryId(product.getCategory().getId())
                    .createdAt(product.getCreatedAt())
                    .modifiedAt(product.getModifiedAt())
                    .discountPrice(product.getDiscountPrice())
                    .build();

        return dto;
    }

    @Override
    public List<ProductDTO> getSelectedCategoryItem(Long id) {
        List<Product> dtoLists = productRepository.findrelatedItem(id);
        List<ProductDTO> dtoList = dtoLists.stream()
                .map(this::entityToDTO) // Product를 ProductDTO로 변환
                .collect(Collectors.toList()); // 리스트로 수집
        return dtoList;
    }


    @Transactional(readOnly = true)
    @Override
    public ProductDTO getDTO(Product product) {

        return this.entityToDTO(product);

    }

    @Transactional(readOnly = true)
    @Override
    public Product getEntity(Long productId) {
        return productRepository.findById(productId).
                orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다. productId: " + productId));
    }
}

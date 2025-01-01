package com.friendzoo.api.domain.product.service;

import com.friendzoo.api.domain.product.dto.ProductDTO;
import com.friendzoo.api.domain.product.entity.Category;
import com.friendzoo.api.domain.product.entity.Product;
import com.friendzoo.api.domain.product.entity.ProductImage;
import com.friendzoo.api.domain.product.enums.ProductBest;
import com.friendzoo.api.domain.product.enums.ProductMdPick;
import com.friendzoo.api.dto.PageRequestDTO;
import com.friendzoo.api.dto.PageResponseDTO;

import java.util.List;


public interface AdminProductService {

    PageResponseDTO<ProductDTO> list(PageRequestDTO requestDTO);

    ProductDTO getOne(Long id);

    Long register(ProductDTO productDTO);

    Long modify(Long id, ProductDTO productDTO);

    void remove(Long id);

    /**
     * Product -> ProductDTO 변환
     *
     * @param product Product
     * @return ProductDTO
     */
    default ProductDTO entityToDTO(Product product) {

        ProductDTO productDTO = ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockNumber(product.getStockNumber())
                .createdAt(product.getCreatedAt())
                .modifiedAt(product.getModifiedAt())
                .build();

        List<ProductImage> imageList = product.getImageList();

        if (imageList == null || imageList.isEmpty()) {
            return productDTO;
        }

        List<String> fileNameList = imageList.stream().map(ProductImage::getImageName).toList();

        productDTO.setUploadFileNames(fileNameList);
        productDTO.setCategoryId(product.getCategory().getId());

        return productDTO;
    }

    /**
     * ProductDTO -> Product 변환
     * @param productDTO ProductDTO
     * @param category Category
     * @return Product
     */
    default Product dtoToEntity(ProductDTO productDTO, Category category) {

        Product product = Product.builder()
                .id(productDTO.getId())
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .discountPrice(productDTO.getDiscountPrice() == null ? 0 : productDTO.getDiscountPrice())
                .best(productDTO.getBest() == null ? ProductBest.N : productDTO.getBest())
                .mdPick(productDTO.getMdPick() == null ? ProductMdPick.N : productDTO.getMdPick())
                .stockNumber(productDTO.getStockNumber())
                .delFlag(false)
                .category(category)
                .build();
        //업로드 처리가 끝난 파일들의 이름 리스트
        List<String> uploadFileNames = productDTO.getUploadFileNames();

        if (uploadFileNames == null) {
            return product;
        }

        uploadFileNames.forEach(product::addImageString);

        return product;
    }

}

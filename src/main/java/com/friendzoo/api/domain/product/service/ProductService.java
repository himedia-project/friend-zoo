package com.friendzoo.api.domain.product.service;

import com.friendzoo.api.domain.member.dto.JoinRequestDTO;
import com.friendzoo.api.domain.member.dto.MemberTestDTO;
import com.friendzoo.api.domain.member.entity.Member;
import com.friendzoo.api.domain.product.dto.CategoryDTO;
import com.friendzoo.api.domain.product.dto.ProductDTO;
import com.friendzoo.api.domain.product.entity.Category;
import com.friendzoo.api.domain.product.entity.Product;
import com.friendzoo.api.domain.test.dto.TestResDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface ProductService {


    List<ProductDTO> getProducts(ProductDTO productDTO);

    List<ProductDTO> getNewProduct(ProductDTO productDTO);

    List<ProductDTO> getSelectedCategory(String name);

    List<ProductDTO> getSelectedItem(Long id);

    List<ProductDTO> getSelectedCategoryItem(Long id);


    default ProductDTO entityToDTO(Product product) {
        if (product == null) {
            return null;
        }

        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory() != null ? product.getCategory().getId() : null)
                .price(product.getPrice())
                .best(product.getBest().toString())
                .mdPick(product.getMdPick().toString())
//                .delFlag(Integer.valueOf(product.getDelFlag().toString()))
//                .imageList(product.getProductImageList().stream().map(ProductImageList::getImageName).toList())
                .discountPrice(product.getDiscountPrice())
                .build();
    }

    @Transactional(readOnly = true)
    ProductDTO getDTO(ProductDTO productDTO);

    @Transactional(readOnly = true)
    ProductDTO getDTO(Product product);


//    // product -> dto
//    default ProductDTO returnProduct(Product product) {
//        return ProductDTO.builder()
//                .id(product.getId())
//                .name(product.getName())
//                .category(product.getCategory().getId())
//                .build();
//    }


}

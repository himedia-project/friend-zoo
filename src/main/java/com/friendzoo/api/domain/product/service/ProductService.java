package com.friendzoo.api.domain.product.service;

import com.friendzoo.api.domain.member.dto.JoinRequestDTO;
import com.friendzoo.api.domain.member.dto.MemberTestDTO;
import com.friendzoo.api.domain.member.entity.Member;
import com.friendzoo.api.domain.product.dto.ProductDTO;
import com.friendzoo.api.domain.product.entity.Product;
import com.friendzoo.api.domain.test.dto.TestResDTO;

import java.util.List;
import java.util.Map;

public interface ProductService {


    List<Product> getProducts(String best);

//    // product -> dto
//    default ProductDTO returnProduct(Product product) {
//        return ProductDTO.builder()
//                .id(product.getId())
//                .name(product.getName())
//                .category(product.getCategory().getId())
//                .build();
//    }


}

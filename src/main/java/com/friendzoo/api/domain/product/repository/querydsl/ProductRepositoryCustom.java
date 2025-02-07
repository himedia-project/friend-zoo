package com.friendzoo.api.domain.product.repository.querydsl;


import com.friendzoo.api.domain.product.entity.Product;
import com.friendzoo.api.dto.PageRequestDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductRepositoryCustom {

    List<Product> findByIdList(List<Long> idList);

    Page<Product> findListBy(PageRequestDTO requestDTO);

    Product findDetailProduct(String email,Long productId);
}

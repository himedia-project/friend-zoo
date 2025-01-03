package com.friendzoo.api.domain.heart.repository.querydsl;

import com.friendzoo.api.domain.product.entity.Product;

import java.util.List;

public interface HeartRepositoryCustom {

    List<Product> findProductListByMember(String email);
}

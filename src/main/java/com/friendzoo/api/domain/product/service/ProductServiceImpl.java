package com.friendzoo.api.domain.product.service;

import com.friendzoo.api.domain.member.repository.MemberRepository;
import com.friendzoo.api.domain.product.dto.ProductDTO;
import com.friendzoo.api.domain.product.entity.Product;
import com.friendzoo.api.domain.product.repository.ProductRepository;
import com.friendzoo.api.props.JwtProps;
import com.friendzoo.api.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final JWTUtil jwtUtil;
    private final JwtProps jwtProps;
    private final ProductRepository productRepository;

    @Override
    public List<Product> getProducts(String best) {
        if (best.equals("Y")) {
            return productRepository.findBestProducts();
        }
        else{
            return productRepository.findAll();
        }
    }
}

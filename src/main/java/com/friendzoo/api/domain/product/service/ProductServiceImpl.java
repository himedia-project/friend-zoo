package com.friendzoo.api.domain.product.service;

import com.friendzoo.api.domain.product.dto.ProductDTO;
import com.friendzoo.api.domain.product.entity.Product;
import com.friendzoo.api.domain.product.repository.ProductRepository;
import com.friendzoo.api.props.JwtProps;
import com.friendzoo.api.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final JWTUtil jwtUtil;
    private final JwtProps jwtProps;
    private final ProductRepository productRepository;

    @Override
    public List<ProductDTO> getProducts(ProductDTO productDTO) {
        if(productDTO.getBest() != null) {
            List<Product> dtoLists = productRepository.findBestProducts();
            List<ProductDTO> dtoList = dtoLists.stream()
                    .map(this::entityToDTO) // Product를 ProductDTO로 변환
                    .collect(Collectors.toList()); // 리스트로 수집
            return dtoList;
        }
        else if(productDTO.getMdPick() != null) {
            List<Product> dtoLists = productRepository.findMdPickProducts();
            List<ProductDTO> dtoList = dtoLists.stream()
                    .map(this::entityToDTO) // Product를 ProductDTO로 변환
                    .collect(Collectors.toList()); // 리스트로 수집
            return dtoList;
        }
        else{
            return null;
        }


//        else{
//            return productRepository.findAll();
//        }
    }

    @Override
    public ProductDTO getDTO(ProductDTO productDTO) {
        return null;
    }


    @Transactional(readOnly = true)
    @Override
    public ProductDTO getDTO(Product product) {

        return this.entityToDTO(product);

    }
}

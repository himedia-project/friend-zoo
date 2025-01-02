package com.friendzoo.api.domain.product.service;

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
    public List<ProductDTO> getSelectedItem(Long id) {
        List<Product> dtoLists = productRepository.findSelectedItem(id);
        List<ProductDTO> dtoList = dtoLists.stream()
                .map(this::entityToDTO) // Product를 ProductDTO로 변환
                .collect(Collectors.toList()); // 리스트로 수집
        return dtoList;
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

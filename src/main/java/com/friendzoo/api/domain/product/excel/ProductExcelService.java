package com.friendzoo.api.domain.product.excel;

import com.friendzoo.api.domain.product.dto.ProductDTO;
import com.friendzoo.api.domain.product.entity.ProductImage;
import com.friendzoo.api.domain.product.repository.ProductRepository;
import com.friendzoo.api.util.excel.RegistrationFailResponseDTO;
import com.friendzoo.api.util.file.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductExcelService {

    private final ProductRepository productRepository;
    private final ProductCreator productCreator;
    private final CustomFileUtil fileUtil;


    public List<RegistrationFailResponseDTO> register(List<ProductDTO> dtoList, String email) {
        List<RegistrationFailResponseDTO> failRowList = new ArrayList<>();

        for (int i = 0; i < dtoList.size(); i++) {
            try {
                productCreator.create(dtoList.get(i));
            } catch (IllegalArgumentException e) {
                // 1행 부터 시작이기 때문에 2를 더한다.
                failRowList.add(new RegistrationFailResponseDTO(i + 2, e.getMessage()));
            } catch (Exception e) {
                failRowList.add(new RegistrationFailResponseDTO(i + 2, "Fail" + e.getMessage()));
            }
        }

        return failRowList;
    }

    public List<ProductDataDTO> getExcelDataList(ProductIdListDTO requestDto) {
        return productRepository.findByIdList(requestDto.getIdList()).stream()
                .map(product -> {
                    ProductDataDTO dto = ProductDataDTO.builder()
                            .id(product.getId())
                            .name(product.getName())
                            .price(product.getPrice())
                            .discountPrice(product.getDiscountPrice())
                            .description(product.getDescription())
                            .imagePathList(fileUtil.getMergedS3ImagePathList(product.getImageList().stream().map(ProductImage::getImageName).toList()))
                            .build();
                    return dto;
                })
                .toList();
    }


}

package com.friendzoo.api.domain.product.excel;

import com.friendzoo.api.domain.product.entity.Product;
import com.friendzoo.api.domain.product.entity.ProductImage;
import com.friendzoo.api.util.excel.ExcelColumn;
import lombok.*;
import org.jetbrains.annotations.NotNull;


import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class ProductDataDTO {

    @ExcelColumn(header = "상품 번호")
    private Long id;

    @ExcelColumn(header = "제목")
    private String name;

    @ExcelColumn(header = "가격")
    private int price;

    @ExcelColumn(header = "할인가격")
    private int discountPrice;

    @ExcelColumn(header = "설명")
    private String description;

    @ExcelColumn(header = "이미지 경로 리스트")
    private String imagePathList;

    public static ProductDataDTO from(Product product) {
        return ProductDataDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .discountPrice(product.getDiscountPrice())
                .description(product.getDescription())
                .imagePathList(getMergeStr(product.getImageList().stream().map(ProductImage::getImageName).toList()))
                .build();
    }

    private static String getMergeStr(@NotNull List<String> list) {
        return String.join(",", list);
    }
}

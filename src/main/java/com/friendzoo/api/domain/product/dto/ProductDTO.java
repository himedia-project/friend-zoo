package com.friendzoo.api.domain.product.dto;

import com.friendzoo.api.domain.product.enums.ProductBest;
import com.friendzoo.api.domain.product.enums.ProductMdPick;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ProductDTO {
    private Long id;
    private Long category;
    private String name;
    private Integer price;
//    private List<String> imageList;
    private ProductBest best;
    private String description;
    private Integer discountPrice;
    private Integer stockNumber;
    private Long categoryId;
//    private Integer delFlag;
//    private List
    private ProductMdPick mdPick;

    // 파일 입력값
    @Builder.Default
    private List<MultipartFile> files = new ArrayList<>();

    // 파일 업로드한 url 응답값
    @Builder.Default
    private List<String> uploadFileNames = new ArrayList<>();



//    public String getBest() {
//        return best;
//    }
//
//    public void setBest(String best) {
//        this.best = best;
//    }



}

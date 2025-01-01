package com.friendzoo.api.domain.product.dto;

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
    private String best;
    private String description;
    private Integer discountPrice;
    private Integer stockNumber;
    private Long categoryId;
//    private Integer delFlag;
//    private List
    private String mdPick;

    @Builder.Default
    private List<MultipartFile> files = new ArrayList<>();

    @Builder.Default
    private List<String> uploadFileNames = new ArrayList<>();


    public String getBest() {
        return best;
    }

    public void setBest(String best) {
        this.best = best;
    }



}

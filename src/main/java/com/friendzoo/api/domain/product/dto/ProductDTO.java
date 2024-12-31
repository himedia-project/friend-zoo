package com.friendzoo.api.domain.product.dto;

import lombok.*;

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
    private Integer discountPrice;
//    private Integer delFlag;
//    private List
    private String mdPick;

    public String getBest() {
        return best;
    }

    public void setBest(String best) {
        this.best = best;
    }



}

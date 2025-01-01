package com.friendzoo.api.domain.product.entity;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {

    private String imageName;

    private Integer ord;

    public void setOrd(int ord){
        this.ord = ord;
    }

}

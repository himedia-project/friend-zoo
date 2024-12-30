package com.friendzoo.api.domain.product.dto;

import com.friendzoo.api.domain.member.enums.MemberRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

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

    private String best;

    public String getBest() {
        return best;
    }

    public void setBest(String best) {
        this.best = best;
    }
}

package com.friendzoo.api.domain.product.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class HeartDTO {
    private Long heartId;
    private String email;
    private Long productId;
    private Long contentId;


}

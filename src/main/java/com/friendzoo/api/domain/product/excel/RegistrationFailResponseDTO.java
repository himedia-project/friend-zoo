package com.friendzoo.api.domain.product.excel;

import lombok.Getter;

@Getter
public class RegistrationFailResponseDTO {
    private final int row;
    private final String errorMessage;

    public RegistrationFailResponseDTO(int row, String errorMessage) {
        this.row = row;
        this.errorMessage = errorMessage;
    }
}

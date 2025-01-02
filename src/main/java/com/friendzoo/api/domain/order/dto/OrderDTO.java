package com.friendzoo.api.domain.order.dto;

import com.friendzoo.api.domain.cart.dto.CartItemDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private List<@Valid CartItemDTO> cartItems;
}

package com.bookstore.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateOrderRequestDto {
    @NotNull
    @Schema(example = "Ukraine, Lviv, Market Square")
    @Size(min = 10, max = 255,
            message = "Shipping address  should be between 10 and 255 characters")
    private String shippingAddress;
}

package com.bookstore.dto.item;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemRequestDtoWithoutBookId {
    @NotNull
    @Min(1)
    private int quantity;
}

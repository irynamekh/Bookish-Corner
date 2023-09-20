package com.bookstore.dto.item;

import lombok.Data;

@Data
public class CartItemRequestDto {
    private Long bookId;
    private int quantity;
}

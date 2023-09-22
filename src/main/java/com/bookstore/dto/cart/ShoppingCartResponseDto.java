package com.bookstore.dto.cart;

import com.bookstore.dto.cartitem.CartItemResponseDto;
import java.util.Set;
import lombok.Data;

@Data
public class ShoppingCartResponseDto {
    private Long id;
    private Set<CartItemResponseDto> cartItems;
}

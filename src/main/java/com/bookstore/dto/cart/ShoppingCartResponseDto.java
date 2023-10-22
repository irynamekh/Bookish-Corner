package com.bookstore.dto.cart;

import com.bookstore.dto.cartitem.CartItemResponseDto;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ShoppingCartResponseDto {
    private Long id;
    private Set<CartItemResponseDto> cartItems;
}

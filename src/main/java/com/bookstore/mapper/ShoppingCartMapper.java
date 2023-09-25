package com.bookstore.mapper;

import com.bookstore.config.MapperConfig;
import com.bookstore.dto.cart.ShoppingCartResponseDto;
import com.bookstore.model.ShoppingCart;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    ShoppingCartResponseDto toDto(ShoppingCart shoppingCart);
}

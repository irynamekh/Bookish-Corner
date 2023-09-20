package com.bookstore.mapper;

import com.bookstore.config.MapperConfig;
import com.bookstore.dto.item.CartItemResponseDto;
import com.bookstore.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    @Mapping(source = "book.title", target = "bookTitle")
    @Mapping(source = "book.id", target = "bookId")
    CartItemResponseDto toDto(CartItem cartItem);
}

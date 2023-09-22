package com.bookstore.mapper;

import com.bookstore.config.MapperConfig;
import com.bookstore.dto.orderitem.OrderItemResponseDto;
import com.bookstore.model.CartItem;
import com.bookstore.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(source = "book.price", target = "price")
    OrderItem fromCartItem(CartItem cartItem);

    @Mapping(source = "book.id", target = "bookId")
    OrderItemResponseDto toDto(OrderItem orderItem);
}

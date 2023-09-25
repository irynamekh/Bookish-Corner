package com.bookstore.service.cart;

import com.bookstore.dto.cart.ShoppingCartResponseDto;
import com.bookstore.dto.item.CartItemRequestDto;
import com.bookstore.dto.item.CartItemRequestDtoWithoutBookId;
import com.bookstore.model.User;

public interface ShoppingCartService {
    void createCart(User user);

    ShoppingCartResponseDto findCart();

    ShoppingCartResponseDto saveItem(CartItemRequestDto requestDto);

    ShoppingCartResponseDto update(Long id, CartItemRequestDtoWithoutBookId categoryDto);

    void deleteItem(Long id);
}

package com.bookstore.service.cart;

import com.bookstore.dto.cart.ShoppingCartResponseDto;
import com.bookstore.dto.cartitem.CartItemRequestDto;
import com.bookstore.dto.cartitem.CartItemRequestDtoWithoutBookId;
import com.bookstore.dto.cartitem.CartItemResponseDto;
import com.bookstore.model.ShoppingCart;
import com.bookstore.model.User;

public interface ShoppingCartService {
    void createCart(User user);

    ShoppingCartResponseDto findCart();

    ShoppingCartResponseDto saveItem(CartItemRequestDto requestDto);

    CartItemResponseDto update(Long id, CartItemRequestDtoWithoutBookId categoryDto);

    void deleteItem(Long id);

    ShoppingCart getById(Long id);
}

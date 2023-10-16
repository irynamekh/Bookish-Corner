package com.bookstore.controller;

import com.bookstore.dto.cart.ShoppingCartResponseDto;
import com.bookstore.dto.cartitem.CartItemRequestDto;
import com.bookstore.dto.cartitem.CartItemRequestDtoWithoutBookId;
import com.bookstore.dto.cartitem.CartItemResponseDto;
import com.bookstore.model.User;
import com.bookstore.service.cart.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ShoppingCart management", description = "Endpoints for managing shopping carts")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get shopping cart",
            description = "Get users shopping cart with its items")
    public ShoppingCartResponseDto getShoppingCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.findCart(user.getId());
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Create new shopping cart item",
            description = "Create new item in shopping cart")
    public ShoppingCartResponseDto createCartItem(
            @RequestBody @Valid CartItemRequestDto requestDto,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.saveItem(requestDto, user.getId());
    }

    @PutMapping("/cart-items/{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Change item",
            description = "Change book quantity in item by id")
    public CartItemResponseDto updateItemQuantity(
            @PathVariable Long id,
            @RequestBody @Valid CartItemRequestDtoWithoutBookId requestDto,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.update(id, requestDto, user.getId());
    }

    @DeleteMapping("/cart-items/{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Delete item", description = "Delete book from cart by item id")
    public void deleteItem(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        shoppingCartService.deleteItem(id, user.getId());
    }
}

package com.bookstore.service.cart;

import com.bookstore.dto.cart.ShoppingCartResponseDto;
import com.bookstore.dto.cartitem.CartItemRequestDto;
import com.bookstore.dto.cartitem.CartItemRequestDtoWithoutBookId;
import com.bookstore.dto.cartitem.CartItemResponseDto;
import com.bookstore.exception.EntityNotFoundException;
import com.bookstore.mapper.CartItemMapper;
import com.bookstore.mapper.ShoppingCartMapper;
import com.bookstore.model.CartItem;
import com.bookstore.model.ShoppingCart;
import com.bookstore.model.User;
import com.bookstore.repository.CartItemRepository;
import com.bookstore.repository.ShoppingCartRepository;
import com.bookstore.security.AuthentificationService;
import com.bookstore.service.book.BookService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final BookService bookService;
    private final AuthentificationService authentificationService;

    @Override
    public void createCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(user.getId());
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto findCart() {
        return shoppingCartMapper.toDto(getShoppingCart());
    }

    @Transactional
    @Override
    public ShoppingCartResponseDto saveItem(CartItemRequestDto requestDto) {
        ShoppingCart cart = getShoppingCart();
        CartItem cartItem = new CartItem();
        cartItem.setShoppingCart(cart);
        cartItem.setBook(bookService.getBookById(requestDto.getBookId()));
        cartItem.setQuantity(requestDto.getQuantity());

        CartItemResponseDto itemDto = cartItemMapper.toDto(cartItemRepository.save(cartItem));
        ShoppingCartResponseDto cartDto = shoppingCartMapper.toDto(cart);
        cartDto.getCartItems().add(itemDto);
        return cartDto;
    }

    @Transactional
    @Override
    public CartItemResponseDto update(Long id, CartItemRequestDtoWithoutBookId requestDto) {
        ShoppingCart cart = getShoppingCart();
        CartItem cartItem = cart.getCartItems().stream()
                .filter(c -> Objects.equals(c.getId(), id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find cart item by id: " + id));
        cartItem.setQuantity(requestDto.getQuantity());
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void deleteItem(Long id) {
        cartItemRepository.deleteById(id);
    }

    @Override
    public ShoppingCart getById(Long id) {
        return shoppingCartRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find shopping cart by id: " + id));
    }

    private ShoppingCart getShoppingCart() {
        return shoppingCartRepository.findById(authentificationService.getUserId())
                .orElse(new ShoppingCart());
    }
}

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

    @Override
    public void createCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(user.getId());
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto findCart(Long userId) {
        return shoppingCartMapper.toDto(getShoppingCartById(userId));
    }

    @Transactional
    @Override
    public ShoppingCartResponseDto saveItem(CartItemRequestDto requestDto, Long userId) {
        CartItem cartItem = new CartItem();
        cartItem.setShoppingCart(getShoppingCartById(userId));
        cartItem.setBook(bookService.getBookById(requestDto.getBookId()));
        cartItem.setQuantity(requestDto.getQuantity());

        CartItemResponseDto itemDto = cartItemMapper.toDto(cartItemRepository.save(cartItem));
        ShoppingCartResponseDto cartDto = shoppingCartMapper.toDto(getShoppingCartById(userId));
        cartDto.getCartItems().add(itemDto);
        return cartDto;
    }

    @Transactional
    @Override
    public CartItemResponseDto update(Long id, CartItemRequestDtoWithoutBookId requestDto,
                                      Long userId) {
        CartItem cartItem = getShoppingCartById(userId).getCartItems().stream()
                .filter(c -> Objects.equals(c.getId(), id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find cart item by id: " + id));
        cartItem.setQuantity(requestDto.getQuantity());
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void deleteItem(Long id, Long userId) {
        existsItemByIdAndShoppingCartId(id, userId);
        cartItemRepository.deleteById(id);
    }

    public ShoppingCart getShoppingCartById(Long id) {
        return shoppingCartRepository.findById(id)
                .orElseGet(() -> shoppingCartRepository.save(new ShoppingCart()));
    }

    private void existsItemByIdAndShoppingCartId(Long itemId, Long cartId) {
        getShoppingCartById(cartId).getCartItems().stream()
                .filter(cartItem -> cartItem.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find shopping cart item by id: " + itemId));
    }
}

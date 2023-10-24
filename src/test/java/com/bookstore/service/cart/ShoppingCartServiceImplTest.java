package com.bookstore.service.cart;

import com.bookstore.dto.cart.ShoppingCartResponseDto;
import com.bookstore.dto.cartitem.CartItemRequestDto;
import com.bookstore.dto.cartitem.CartItemRequestDtoWithoutBookId;
import com.bookstore.dto.cartitem.CartItemResponseDto;
import com.bookstore.mapper.CartItemMapper;
import com.bookstore.mapper.ShoppingCartMapper;
import com.bookstore.model.Book;
import com.bookstore.model.CartItem;
import com.bookstore.model.Category;
import com.bookstore.model.ShoppingCart;
import com.bookstore.model.User;
import com.bookstore.repository.CartItemRepository;
import com.bookstore.repository.ShoppingCartRepository;
import com.bookstore.service.book.BookService;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceImplTest {
    private static final Long VALID_ID = 2L;
    private static final User VALID_USER = new User()
            .setId(2L);
    private static final CartItem VALID_CART_ITEM = new CartItem()
            .setId(VALID_ID);
    private static final String TITLE = "Colony";
    private static final String AUTHOR = "Max Kidruk";
    private static final String ISBN = "9786176798323";
    private static final int VALID_QUANTITY = 5;
    private static final BigDecimal PRICE = BigDecimal.valueOf(699.00);
    private static final Category VALID_CATEGORY = new Category()
            .setId(VALID_ID)
            .setName("Science Fiction");
    private static final Book VALID_BOOK = new Book()
            .setId(VALID_ID)
            .setTitle(TITLE)
            .setAuthor(AUTHOR)
            .setIsbn(ISBN)
            .setPrice(PRICE)
            .setCategories(Set.of(VALID_CATEGORY));

    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private CartItemMapper cartItemMapper;
    @Mock
    private BookService bookService;
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @Test
    @DisplayName("Valid case of the createCart method run")
    void createCart_validUser_isOk() {
        when(shoppingCartRepository.save(buildValidCart())).thenReturn(buildValidCart());

        shoppingCartService.createCart(VALID_USER);

        verify(shoppingCartRepository).save(buildValidCart());
    }

    @Test
    @DisplayName("Valid case of the findCart method run")
    void findCart_validCartId_returnsValidShoppingCartResponseDto() {
        when(shoppingCartMapper.toDto(buildValidCart()))
                .thenReturn(buildValidCartResponseDto());
        when(shoppingCartRepository.findById(VALID_ID))
                .thenReturn(Optional.ofNullable(buildValidCart()));

        ShoppingCartResponseDto expected = buildValidCartResponseDto();
        ShoppingCartResponseDto actual = shoppingCartService.findCart(VALID_ID);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Valid case of the saveItem method run")
    void saveItem_validRequeatDtoAndId_returnsValidCartResponseDto() {
        when(shoppingCartMapper.toDto(buildValidCart()))
                .thenReturn(buildValidCartResponseDto());
        when(shoppingCartRepository.findById(VALID_ID))
                .thenReturn(Optional.ofNullable(buildValidCart()));
        when(bookService.getBookById(VALID_ID))
                .thenReturn(VALID_BOOK);
        when(cartItemMapper.toDto(buildValidItem()))
                .thenReturn(buildValidItemResponseDto());
        when(cartItemRepository.save(buildValidItem()))
                .thenReturn(buildValidItem());

        ShoppingCartResponseDto expected = buildValidCartResponseDto();
        ShoppingCartResponseDto actual = shoppingCartService
                .saveItem(buildValidItemRequestDto(), VALID_ID);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Valid case of the update method run")
    void update_validInput_returnsValidCartItemResponseDto() {
        when(shoppingCartRepository.findById(VALID_ID))
                .thenReturn(Optional.ofNullable(buildValidCart()));
        when(cartItemMapper.toDto(VALID_CART_ITEM))
                .thenReturn(buildValidItemResponseDto());
        when(cartItemRepository.save(VALID_CART_ITEM))
                .thenReturn(VALID_CART_ITEM);

        CartItemResponseDto expected = buildValidItemResponseDto();
        CartItemResponseDto actual = shoppingCartService.update(VALID_ID,
                buildValidItemRequestDtoWithoutBookId(),VALID_USER.getId());

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Valid case of the deleteItem method run")
    void deleteItem_validIdAndUserId_isOk() {
        when(shoppingCartRepository.findById(VALID_ID))
                .thenReturn(Optional.ofNullable(buildValidCart()));
        doNothing().when(cartItemRepository).deleteById(VALID_ID);

        shoppingCartService.deleteItem(VALID_ID, VALID_USER.getId());
        verify(cartItemRepository).deleteById(VALID_ID);
    }

    private ShoppingCart buildValidCart() {
        return new ShoppingCart()
                .setId(VALID_ID)
                .setUser(VALID_USER)
                .setCartItems(setCartItems());
    }

    private ShoppingCartResponseDto buildValidCartResponseDto() {
        return new ShoppingCartResponseDto()
                .setCartItems(setCartItemResponseDtos());
    }

    private CartItemRequestDto buildValidItemRequestDto() {
        return new CartItemRequestDto()
                .setBookId(VALID_ID)
                .setQuantity(VALID_QUANTITY);
    }

    private CartItemRequestDtoWithoutBookId buildValidItemRequestDtoWithoutBookId() {
        return new CartItemRequestDtoWithoutBookId()
                .setQuantity(VALID_QUANTITY);
    }

    private static CartItemResponseDto buildValidItemResponseDto() {
        return new CartItemResponseDto()
                .setBookTitle(TITLE)
                .setBookId(VALID_ID)
                .setQuantity(VALID_QUANTITY);
    }

    private CartItem buildValidItem() {
        return new CartItem()
                .setShoppingCart(buildValidCart())
                .setBook(VALID_BOOK)
                .setQuantity(VALID_QUANTITY);
    }

    private Set<CartItemResponseDto> setCartItemResponseDtos() {
        Set<CartItemResponseDto> dtos = new HashSet<>();
        dtos.add(buildValidItemResponseDto());
        return dtos;
    }

    private Set<CartItem> setCartItems() {
        Set<CartItem> dtos = new HashSet<>();
        dtos.add(VALID_CART_ITEM);
        return dtos;
    }
}

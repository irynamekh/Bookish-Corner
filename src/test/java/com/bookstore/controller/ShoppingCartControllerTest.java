package com.bookstore.controller;

import com.bookstore.dto.cart.ShoppingCartResponseDto;
import com.bookstore.dto.cartitem.CartItemRequestDto;
import com.bookstore.dto.cartitem.CartItemRequestDtoWithoutBookId;
import com.bookstore.dto.cartitem.CartItemResponseDto;
import com.bookstore.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShoppingCartControllerTest {
    private static final Long VALID_ID = 1L;
    private static final Long USER_ID = 2L;
    private static final String TITLE = "Colony";
    private static final int VALID_QUANTITY = 5;
    private static final String ADD_USER_CART_AND_CART_ITEMS =
            "classpath:database/cart&cartitems/add_user_cart_and_cartitems.sql";
    private static final String DELETE_USER_CART_AND_CART_ITEMS =
            "classpath:database/cart&cartitems/delete_user_cart_and_cartitems.sql";
    private static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtUtil jwtUtil;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Valid case of the getShoppingCart method run with valid request")
    @Sql(scripts = ADD_USER_CART_AND_CART_ITEMS,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_USER_CART_AND_CART_ITEMS,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getShoppingCart_validRequest_isOk() throws Exception {
        String token = jwtUtil.generateToken("user@example.com");

        ShoppingCartResponseDto expected = buildValidCartResponseDto();
        MvcResult result = mockMvc.perform(get("/cart")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        ShoppingCartResponseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), ShoppingCartResponseDto.class);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Valid case of the createCartItem method run with valid request")
    @Sql(scripts = ADD_USER_CART_AND_CART_ITEMS,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_USER_CART_AND_CART_ITEMS,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCartItem_validRequest_isOk() throws Exception {
        String token = jwtUtil.generateToken("user@example.com");

        ShoppingCartResponseDto expected = buildValidCartResponseDto(VALID_ID);
        MvcResult result = mockMvc.perform(post("/cart")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .content(objectMapper.writeValueAsString(buildValidItemRequestDto()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        ShoppingCartResponseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), ShoppingCartResponseDto.class);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Valid case of the updateItemQuantity method run with valid request")
    @Sql(scripts = ADD_USER_CART_AND_CART_ITEMS,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_USER_CART_AND_CART_ITEMS,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateItemQuantity_validRequest_isOk() throws Exception {
        String token = jwtUtil.generateToken("user@example.com");

        CartItemResponseDto expected = buildValidItemResponseDto(VALID_ID);
        MvcResult result = mockMvc.perform(put("/cart/cart-items/{id}", VALID_ID)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .content(objectMapper.writeValueAsString(
                                buildCartItemRequestDtoWithoutBookId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CartItemResponseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CartItemResponseDto.class);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Valid case of the deleteItem method run with valid request")
    @Sql(scripts = ADD_USER_CART_AND_CART_ITEMS,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_USER_CART_AND_CART_ITEMS,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteItem_validRequest_isOk() throws Exception {
        String token = jwtUtil.generateToken("user@example.com");

        CartItemResponseDto expected = buildValidItemResponseDto(VALID_ID);
        MvcResult result = mockMvc.perform(delete("/cart/cart-items/{id}", VALID_ID)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .content(objectMapper.writeValueAsString(VALID_ID))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    private ShoppingCartResponseDto buildValidCartResponseDto() {
        return new ShoppingCartResponseDto()
                .setId(USER_ID)
                .setCartItems(setCartItemResponseDtos());
    }

    private ShoppingCartResponseDto buildValidCartResponseDto(Long id) {
        Set<CartItemResponseDto> dtos = new HashSet<>();
        dtos.add(buildValidItemResponseDto(id));
        dtos.add(buildValidItemResponseDto(id + id));
        return new ShoppingCartResponseDto()
                .setId(USER_ID)
                .setCartItems(dtos);
    }

    private CartItemRequestDto buildValidItemRequestDto() {
        return new CartItemRequestDto()
                .setBookId(VALID_ID)
                .setQuantity(VALID_QUANTITY);
    }

    private Set<CartItemResponseDto> setCartItemResponseDtos() {
        Set<CartItemResponseDto> dtos = new HashSet<>();
        dtos.add(buildValidItemResponseDto(VALID_ID));
        return dtos;
    }

    private static CartItemResponseDto buildValidItemResponseDto(Long id) {
        return new CartItemResponseDto()
                .setId(id)
                .setBookTitle(TITLE)
                .setBookId(VALID_ID)
                .setQuantity(VALID_QUANTITY);
    }

    private CartItemRequestDtoWithoutBookId buildCartItemRequestDtoWithoutBookId() {
        return new CartItemRequestDtoWithoutBookId()
                .setQuantity(VALID_QUANTITY);
    }
}

package com.bookstore.controller;

import com.bookstore.dto.book.BookRequestDto;
import com.bookstore.dto.book.BookResponseDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    private static final Long VALID_ID = 1L;
    private static final String TITLE = "Colony";
    private static final String AUTHOR = "Max Kidruk";
    private static final String ISBN = "9786176798323";
    private static final String CREATED_ISBN = "9786176798330";
    private static final BigDecimal PRICE = BigDecimal.valueOf(699);
    private static final BigDecimal UPDATED_PRICE = BigDecimal.valueOf(599);
    private static final String ADD_BOOK_AND_CATEGORY_SCRIPT =
            "classpath:database/book&category/add_book_and_category.sql";
    private static final String DELETE_BOOK_AND_CATEGORY_SCRIPT =
            "classpath:database/book&category/delete_book_and_category.sql";
    private static final BookRequestDto VALID_BOOK_REQUEST_DTO = new BookRequestDto()
                    .setTitle(TITLE)
                    .setAuthor(AUTHOR)
                    .setIsbn(ISBN)
                    .setPrice(PRICE)
                    .setCategoryIds(Set.of(VALID_ID));
    private static final BookRequestDto CREATED_BOOK_REQUEST_DTO = new BookRequestDto()
            .setTitle(TITLE)
            .setAuthor(AUTHOR)
            .setIsbn(CREATED_ISBN)
            .setPrice(PRICE)
            .setCategoryIds(Set.of(VALID_ID));
    private static final BookRequestDto UPDATED_BOOK_REQUEST_DTO = new BookRequestDto()
            .setTitle(TITLE)
            .setAuthor(AUTHOR)
            .setIsbn(ISBN)
            .setPrice(UPDATED_PRICE)
            .setCategoryIds(Set.of(VALID_ID));
    private static final BookResponseDto VALID_BOOK_RESPONSE_DTO = new BookResponseDto()
            .setId(VALID_ID)
            .setTitle(TITLE)
            .setAuthor(AUTHOR)
            .setIsbn(ISBN)
            .setPrice(PRICE)
            .setCategoryIds(Set.of(VALID_ID));
    private static final BookResponseDto CREATED_BOOK_RESPONSE_DTO = new BookResponseDto()
            .setId(VALID_ID)
            .setTitle(TITLE)
            .setAuthor(AUTHOR)
            .setIsbn(CREATED_ISBN)
            .setPrice(PRICE)
            .setCategoryIds(Set.of(VALID_ID));
    private static final BookResponseDto UPDATED_BOOK_RESPONSE_DTO = new BookResponseDto()
            .setId(VALID_ID)
            .setTitle(TITLE)
            .setAuthor(AUTHOR)
            .setIsbn(ISBN)
            .setPrice(UPDATED_PRICE)
            .setCategoryIds(Set.of(VALID_ID));
    private static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Valid case of the getAll method run with valid request")
    @WithMockUser(username = "admin", roles = {"ADMIN, USER"})
    @Sql(scripts = ADD_BOOK_AND_CATEGORY_SCRIPT,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_BOOK_AND_CATEGORY_SCRIPT,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAll_validRequest_returnsValidListDtos() throws Exception {
        List<BookResponseDto> expected = List.of(VALID_BOOK_RESPONSE_DTO);

        MvcResult result = mockMvc.perform(get("/books?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        List<BookResponseDto> actual = objectMapper.readValue(result.getResponse()
                        .getContentAsString(), new TypeReference<>() {});

        assertTrue(EqualsBuilder.reflectionEquals(expected.get(0), actual.get(0), "id"));
    }

    @Test
    @DisplayName("Valid case of the getBookById method run with valid request")
    @WithMockUser(username = "admin", roles = {"ADMIN, USER"})
    @Sql(scripts = ADD_BOOK_AND_CATEGORY_SCRIPT,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_BOOK_AND_CATEGORY_SCRIPT,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBookById_validRequest_returnsValidDto() throws Exception {
        MvcResult result = mockMvc.perform(get("/books/{id}", VALID_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookResponseDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookResponseDto.class);

        assertTrue(EqualsBuilder.reflectionEquals(VALID_BOOK_RESPONSE_DTO, actual, "id"));
    }

    @Test
    @DisplayName("Valid case of the createBook method run with valid request")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = ADD_BOOK_AND_CATEGORY_SCRIPT,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_BOOK_AND_CATEGORY_SCRIPT,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_validRequest_returnsValidDto() throws Exception {
        MvcResult result = mockMvc.perform(post("/books")
                        .content(objectMapper.writeValueAsString(CREATED_BOOK_REQUEST_DTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookResponseDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookResponseDto.class);

        assertTrue(EqualsBuilder.reflectionEquals(CREATED_BOOK_RESPONSE_DTO, actual, "id"));
    }

    @Test
    @DisplayName("Valid case of the updateBook method run with valid request")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = ADD_BOOK_AND_CATEGORY_SCRIPT,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_BOOK_AND_CATEGORY_SCRIPT,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateBook_validRequest_returnsValidDto() throws Exception {
        MvcResult result = mockMvc.perform(put("/books/{id}", VALID_ID)
                        .content(objectMapper.writeValueAsString(UPDATED_BOOK_REQUEST_DTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookResponseDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookResponseDto.class);

        assertTrue(EqualsBuilder.reflectionEquals(UPDATED_BOOK_RESPONSE_DTO, actual, "id"));
    }

    @Test
    @DisplayName("Valid case of the deleteBookById method run with valid request")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = ADD_BOOK_AND_CATEGORY_SCRIPT,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_BOOK_AND_CATEGORY_SCRIPT,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteBookById_validRequest_isOk() throws Exception {
        MvcResult result = mockMvc.perform(delete("/books/{id}", VALID_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }
}

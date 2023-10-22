package com.bookstore.controller;

import com.bookstore.dto.book.BookResponseDtoWithoutCategoryIds;
import com.bookstore.dto.category.CategoryRequestDto;
import com.bookstore.dto.category.CategoryResponseDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
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
class CategoryControllerTest {
    private static final Long VALID_ID = 1L;
    private static final String TITLE = "Colony";
    private static final String AUTHOR = "Max Kidruk";
    private static final String ISBN = "9786176798323";
    private static final String UPDATED_NAME = "Fiction";
    private static final BigDecimal PRICE = BigDecimal.valueOf(699);
    private static final String VALID_NAME = "Science Fiction";
    private static final String VALID_DESCRIPTION = "Imaginative and futuristic concepts "
            + "such as space exploration and time travel.";
    private static final String ADD_BOOK_AND_CATEGORY_SCRIPT =
            "classpath:database/book&category/add_book_and_category.sql";
    private static final String DELETE_BOOK_AND_CATEGORY_SCRIPT =
            "classpath:database/book&category/delete_book_and_category.sql";
    private static final CategoryRequestDto VALID_CATEGORY_REQUEST_DTO = new CategoryRequestDto()
            .setName(VALID_NAME)
            .setDescription(VALID_DESCRIPTION);
    private static final CategoryRequestDto UPDATED_CATEGORY_REQUEST_DTO = new CategoryRequestDto()
            .setName(UPDATED_NAME)
            .setDescription(VALID_DESCRIPTION);
    private static final CategoryResponseDto VALID_CATEGORY_RESPONSE_DTO =
            new CategoryResponseDto(VALID_ID, VALID_NAME, VALID_DESCRIPTION);
    private static final CategoryResponseDto UPDATED_CATEGORY_RESPONSE_DTO =
            new CategoryResponseDto(VALID_ID, UPDATED_NAME, VALID_DESCRIPTION);
    private static final BookResponseDtoWithoutCategoryIds VALID_BOOK_DTO_WITHOUT_CATEGORY_IDS =
            new BookResponseDtoWithoutCategoryIds()
                    .setId(VALID_ID)
                    .setTitle(TITLE)
                    .setAuthor(AUTHOR)
                    .setIsbn(ISBN)
                    .setPrice(PRICE);
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
        List<CategoryResponseDto> expected = List.of(VALID_CATEGORY_RESPONSE_DTO);

        MvcResult result = mockMvc.perform(get("/categories?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        List<CategoryResponseDto> actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), new TypeReference<>() {});

        assertTrue(EqualsBuilder.reflectionEquals(expected.get(0), actual.get(0), "id"));
    }

    @Test
    @DisplayName("Valid case of the getCategoryById method run with valid request")
    @WithMockUser(username = "admin", roles = {"ADMIN, USER"})
    @Sql(scripts = ADD_BOOK_AND_CATEGORY_SCRIPT,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_BOOK_AND_CATEGORY_SCRIPT,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getCategoryById_validRequest_returnsValidDto() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories/" + VALID_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CategoryResponseDto actual = objectMapper.readValue(result.getResponse()
                        .getContentAsString(), CategoryResponseDto.class);

        assertTrue(EqualsBuilder.reflectionEquals(VALID_CATEGORY_RESPONSE_DTO, actual, "id"));
    }

    @Test
    @DisplayName("Valid case of the getBooksByCategoryId method run with valid request")
    @WithMockUser(username = "admin", roles = {"ADMIN, USER"})
    @Sql(scripts = ADD_BOOK_AND_CATEGORY_SCRIPT,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_BOOK_AND_CATEGORY_SCRIPT,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBooksByCategoryId_validRequest_returnsValidListDtos() throws Exception {
        List<BookResponseDtoWithoutCategoryIds> expected =
                List.of(VALID_BOOK_DTO_WITHOUT_CATEGORY_IDS);

        MvcResult result = mockMvc.perform(get("/categories/" + VALID_ID + "/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        List<BookResponseDtoWithoutCategoryIds> actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), new TypeReference<>() {});

        assertTrue(EqualsBuilder.reflectionEquals(expected.get(0), actual.get(0), "id"));
    }

    @Test
    @DisplayName("Valid case of the createCategory method run with valid request")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = DELETE_BOOK_AND_CATEGORY_SCRIPT,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCategory_validRequest_returnsValidDto() throws Exception {
        MvcResult result = mockMvc.perform(post("/categories")
                        .content(objectMapper.writeValueAsString(VALID_CATEGORY_REQUEST_DTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CategoryResponseDto actual = objectMapper.readValue(result.getResponse()
                        .getContentAsString(), CategoryResponseDto.class);

        assertTrue(EqualsBuilder.reflectionEquals(VALID_CATEGORY_RESPONSE_DTO, actual, "id"));
    }

    @Test
    @DisplayName("Valid case of the updateCategory method run with valid request")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = ADD_BOOK_AND_CATEGORY_SCRIPT,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_BOOK_AND_CATEGORY_SCRIPT,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateCategory_validRequest_returnsValidDto() throws Exception {
        MvcResult result = mockMvc.perform(put("/categories/" + VALID_ID)
                        .content(objectMapper.writeValueAsString(UPDATED_CATEGORY_REQUEST_DTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CategoryResponseDto actual = objectMapper.readValue(result.getResponse()
                        .getContentAsString(), CategoryResponseDto.class);

        assertTrue(EqualsBuilder.reflectionEquals(UPDATED_CATEGORY_RESPONSE_DTO, actual, "id"));
    }

    @Test
    @DisplayName("Valid case of the deleteCategory method run with valid request")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = ADD_BOOK_AND_CATEGORY_SCRIPT,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_BOOK_AND_CATEGORY_SCRIPT,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteCategory_validRequest_isOk() throws Exception {
        MvcResult result = mockMvc.perform(delete("/categories/" + VALID_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }
}

package com.bookstore.service.category;

import com.bookstore.dto.book.BookResponseDtoWithoutCategoryIds;
import com.bookstore.dto.category.CategoryRequestDto;
import com.bookstore.dto.category.CategoryResponseDto;
import com.bookstore.mapper.CategoryMapper;
import com.bookstore.model.Category;
import com.bookstore.repository.CategoryRepository;
import com.bookstore.service.book.BookService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    private static final Long VALID_ID = 1L;
    private static final String VALID_NAME = "Science Fiction";
    private static final String VALID_DESCRIPTION = "Genre of speculative fiction, "
            + "which typically deals with imaginative and futuristic concepts "
            + "such as advanced science and technology, space exploration, "
            + "time travel, parallel universes, and extraterrestrial life.";
    private static final String TITLE = "Colony";
    private static final String AUTHOR = "Max Kidruk";
    private static final String ISBN = "9786176798323";
    private static final BigDecimal PRICE = BigDecimal.valueOf(699.00);
    private static final Pageable PAGEABLE = PageRequest.of(0, 10);
    private static final Category VALID_CATEGORY = new Category()
            .setId(VALID_ID)
            .setName(VALID_NAME)
            .setDescription(VALID_DESCRIPTION);
    private static final CategoryRequestDto VALID_CATEGORY_REQUEST_DTO = new CategoryRequestDto()
            .setName(VALID_NAME)
            .setDescription(VALID_DESCRIPTION);
    private static final CategoryResponseDto VALID_CATEGORY_RESPONSE_DTO =
            new CategoryResponseDto(VALID_ID, VALID_NAME, VALID_DESCRIPTION);
    private static final BookResponseDtoWithoutCategoryIds VALID_BOOK_DTO_WITHOUT_CATEGORY_IDS =
            new BookResponseDtoWithoutCategoryIds()
                    .setId(VALID_ID)
                    .setTitle(TITLE)
                    .setAuthor(AUTHOR)
                    .setIsbn(ISBN)
                    .setPrice(PRICE);
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @Mock
    private BookService bookService;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Valid case of the findAll method run")
    void findAll_validPageable_returnsValidListCategoryResponseDtos() {
        when(categoryRepository.findAll(PAGEABLE))
                .thenReturn(new PageImpl<>(List.of(VALID_CATEGORY)));
        when(categoryMapper.toDto(VALID_CATEGORY)).thenReturn(VALID_CATEGORY_RESPONSE_DTO);

        List<CategoryResponseDto> expected = List.of(VALID_CATEGORY_RESPONSE_DTO);

        assertEquals(expected, categoryService.findAll(PAGEABLE));
    }

    @Test
    @DisplayName("Valid case of the getById method run")
    void getById_validCategoryID_returnsValidCategoryRequestDto() {
        when(categoryRepository.getReferenceById(VALID_ID)).thenReturn(VALID_CATEGORY);
        when(categoryMapper.toDto(VALID_CATEGORY)).thenReturn(VALID_CATEGORY_RESPONSE_DTO);

        assertEquals(VALID_CATEGORY_RESPONSE_DTO, categoryService.getById(VALID_ID));
    }

    @Test
    @DisplayName("Valid case of the save method run")
    void save_validCategoryRequestDto_returnsValidCategoryRequestDto() {
        when(categoryRepository.save(VALID_CATEGORY)).thenReturn(VALID_CATEGORY);
        when(categoryMapper.toDto(VALID_CATEGORY)).thenReturn(VALID_CATEGORY_RESPONSE_DTO);
        when(categoryMapper.toModel(VALID_CATEGORY_REQUEST_DTO)).thenReturn(VALID_CATEGORY);

        assertEquals(VALID_CATEGORY_RESPONSE_DTO, categoryService.save(VALID_CATEGORY_REQUEST_DTO));
    }

    @Test
    @DisplayName("Valid case of the update method run")
    void update_validCategoryIdAndCategoryRequestDto_returnsValidCategoryRequestDto() {
        when(categoryRepository.save(VALID_CATEGORY)).thenReturn(VALID_CATEGORY);
        when(categoryRepository.findById(VALID_ID)).thenReturn(Optional.of(VALID_CATEGORY));
        when(categoryMapper.toDto(VALID_CATEGORY)).thenReturn(VALID_CATEGORY_RESPONSE_DTO);

        assertEquals(VALID_CATEGORY_RESPONSE_DTO, categoryService.update(
                VALID_ID, VALID_CATEGORY_REQUEST_DTO));
    }

    @Test
    @DisplayName("Valid case of the deleteById method run")
    void deleteById_validCategoryID_isOk() {
        doNothing().when(categoryRepository).deleteById(VALID_ID);
        categoryService.deleteById(VALID_ID);
        verify(categoryRepository).deleteById(VALID_ID);
    }

    @Test
    @DisplayName("Valid case of the getBooksByCategoryId method run")
    void getBooksByCategoryId_validPageableAndCategoryID_returnsValidListDtos() {
        when(bookService.findAllByCategoryId(PAGEABLE, VALID_ID))
                .thenReturn(List.of(VALID_BOOK_DTO_WITHOUT_CATEGORY_IDS));

        List<BookResponseDtoWithoutCategoryIds> expected =
                List.of(VALID_BOOK_DTO_WITHOUT_CATEGORY_IDS);

        assertEquals(expected, categoryService.getBooksByCategoryId(PAGEABLE, VALID_ID));
    }
}

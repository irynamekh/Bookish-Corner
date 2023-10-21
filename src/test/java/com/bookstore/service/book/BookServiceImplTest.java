package com.bookstore.service.book;

import com.bookstore.dto.book.BookRequestDto;
import com.bookstore.dto.book.BookResponseDto;
import com.bookstore.dto.book.BookResponseDtoWithoutCategoryIds;
import com.bookstore.mapper.BookMapper;
import com.bookstore.model.Book;
import com.bookstore.model.Category;
import com.bookstore.repository.BookRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    private static final Long VALID_ID = 1L;
    private static final String TITLE = "Colony";
    private static final String AUTHOR = "Max Kidruk";
    private static final String ISBN = "9786176798323";
    private static final BigDecimal PRICE = BigDecimal.valueOf(699.00);
    private static final Pageable PAGEABLE = PageRequest.of(0, 10);
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
    private static final BookRequestDto VALID_BOOK_REQUEST_DTO =
            new BookRequestDto()
                    .setTitle(TITLE)
                    .setAuthor(AUTHOR)
                    .setIsbn(ISBN)
                    .setPrice(PRICE)
                    .setCategoryIds(Set.of(VALID_ID));
    private static final BookResponseDto VALID_BOOK_RESPONSE_DTO = new BookResponseDto()
            .setId(VALID_ID)
            .setTitle(TITLE)
            .setAuthor(AUTHOR)
            .setIsbn(ISBN)
            .setPrice(PRICE)
            .setCategoryIds(Set.of(VALID_ID));
    private static final BookResponseDtoWithoutCategoryIds VALID_BOOK_DTO_WITHOUT_CATEGORY_IDS =
            new BookResponseDtoWithoutCategoryIds()
                    .setId(VALID_ID)
                    .setTitle(TITLE)
                    .setAuthor(AUTHOR)
                    .setIsbn(ISBN)
                    .setPrice(PRICE);
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private BookServiceImpl bookServiceImpl;

    @Test
    @DisplayName("Valid case of the findAll method run")
    void findAll_validPageable_returnsValidListBookResponseDtos() {
        when(bookRepository.findAll(PAGEABLE))
                .thenReturn(new PageImpl<>(List.of(VALID_BOOK)));
        when(bookMapper.toDto(VALID_BOOK)).thenReturn(VALID_BOOK_RESPONSE_DTO);

        List<BookResponseDto> expected = List.of(VALID_BOOK_RESPONSE_DTO);

        assertEquals(expected, bookServiceImpl.findAll(PAGEABLE));
    }

    @Test
    @DisplayName("Valid case of the getById method run")
    void getById_validBookID_returnsValidBookResponseDto() {
        when(bookRepository.findById(VALID_ID)).thenReturn(Optional.ofNullable(VALID_BOOK));
        when(bookMapper.toDto(VALID_BOOK)).thenReturn(VALID_BOOK_RESPONSE_DTO);

        assertEquals(VALID_BOOK_RESPONSE_DTO, bookServiceImpl.getById(VALID_ID));
    }

    @Test
    @DisplayName("Valid case of the save method run")
    void save_validBookRequestDto_returnsValidBookResponseDto() {
        when(bookRepository.save(VALID_BOOK)).thenReturn(VALID_BOOK);
        when(bookMapper.toModel(VALID_BOOK_REQUEST_DTO)).thenReturn(VALID_BOOK);
        when(bookMapper.toDto(VALID_BOOK)).thenReturn(VALID_BOOK_RESPONSE_DTO);

        assertEquals(VALID_BOOK_RESPONSE_DTO, bookServiceImpl.save(VALID_BOOK_REQUEST_DTO));
    }

    @Test
    @DisplayName("Valid case of the update method run")
    void update_validBookRequestDtoAndID_returnsValidBookResponseDto() {
        when(bookRepository.save(VALID_BOOK)).thenReturn(VALID_BOOK);
        when(bookRepository.findById(VALID_ID)).thenReturn(Optional.of(VALID_BOOK));
        when(bookMapper.toDto(VALID_BOOK)).thenReturn(VALID_BOOK_RESPONSE_DTO);

        assertEquals(VALID_BOOK_RESPONSE_DTO, bookServiceImpl
                .update(VALID_ID, VALID_BOOK_REQUEST_DTO));
    }

    @Test
    @DisplayName("Valid case of the deleteById method run")
    void deleteById_validBookID_isOk() {
        doNothing().when(bookRepository).deleteById(VALID_ID);
        bookServiceImpl.deleteById(VALID_ID);
        verify(bookRepository, times(1)).deleteById(VALID_ID);
    }

    @Test
    @DisplayName("Valid case of the findAllByCategoryId method run")
    void findAllByCategoryId_validPageableAndCategoryID_returnsValidListDtos() {
        when(bookMapper.toDtoWithoutCategories(VALID_BOOK))
                .thenReturn(VALID_BOOK_DTO_WITHOUT_CATEGORY_IDS);
        when(bookRepository.findAllByCategoryId(PAGEABLE, VALID_ID))
                .thenReturn(List.of(VALID_BOOK));

        List<BookResponseDtoWithoutCategoryIds> expected =
                List.of(VALID_BOOK_DTO_WITHOUT_CATEGORY_IDS);

        assertEquals(expected, bookServiceImpl.findAllByCategoryId(PAGEABLE, VALID_ID));
    }

    @Test
    @DisplayName("Valid case of the getBookById method run")
    void getBookById_validBookID_returnsValidBook() {
        when(bookRepository.findById(VALID_ID)).thenReturn(Optional.of(VALID_BOOK));

        assertEquals(VALID_BOOK, bookServiceImpl.getBookById(VALID_ID));
    }
}

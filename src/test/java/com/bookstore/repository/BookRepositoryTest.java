package com.bookstore.repository;

import com.bookstore.model.Book;
import com.bookstore.model.Category;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    private static final Long VALID_ID = 1L;
    private static final String TITLE = "Colony";
    private static final String AUTHOR = "Max Kidruk";
    private static final String ISBN = "9786176798323";
    private static final BigDecimal PRICE = BigDecimal.valueOf(699);
    private static final Pageable PAGEABLE = PageRequest.of(0, 10);
    private static final String VALID_NAME = "Science Fiction";
    private static final String VALID_DESCRIPTION = "Imaginative and futuristic concepts "
            + "such as space exploration and time travel.";
    private static final Category VALID_CATEGORY = new Category()
            .setId(VALID_ID)
            .setName(VALID_NAME)
            .setDescription(VALID_DESCRIPTION);
    private static final Book VALID_BOOK = new Book()
            .setId(VALID_ID)
            .setTitle(TITLE)
            .setAuthor(AUTHOR)
            .setIsbn(ISBN)
            .setPrice(PRICE)
            .setCategories(Set.of(VALID_CATEGORY));
    private static final String ADD_BOOK_AND_CATEGORY_SCRIPT =
            "classpath:database/book&category/add_book_and_category.sql";
    private static final String DELETE_BOOK_AND_CATEGORY_SCRIPT =
            "classpath:database/book&category/delete_book_and_category.sql";
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Test valid case findAllWithCategories method run")
    @Sql(scripts = ADD_BOOK_AND_CATEGORY_SCRIPT,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_BOOK_AND_CATEGORY_SCRIPT,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllWithCategories_validPageable_returnsValidListBooks() {
        List<Book> expected = List.of(VALID_BOOK);
        List<Book> actual = bookRepository.findAllWithCategories(PAGEABLE);

        assertEquals(expected, actual);
        assertEquals(expected.get(0).getCategories(), actual.get(0).getCategories());
    }

    @Test
    @DisplayName("Test getById method with valid id")
    @Sql(scripts = ADD_BOOK_AND_CATEGORY_SCRIPT,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_BOOK_AND_CATEGORY_SCRIPT,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getById_validBookId_returnsValidBook() {
        Book actual = bookRepository.getById(VALID_ID);

        assertEquals(VALID_BOOK, actual);
    }

    @Test
    @DisplayName("Test findAllByCategoryId method with valid category id")
    @Sql(scripts = ADD_BOOK_AND_CATEGORY_SCRIPT,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_BOOK_AND_CATEGORY_SCRIPT,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByCategoryId_validCategoryId_returnsValidListBooks() {
        List<Book> expected = List.of(VALID_BOOK);
        List<Book> actual = bookRepository.findAllByCategoryId(PAGEABLE, VALID_ID);

        assertEquals(expected, actual);
    }
}

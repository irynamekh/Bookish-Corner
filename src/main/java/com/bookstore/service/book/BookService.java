package com.bookstore.service.book;

import com.bookstore.dto.book.BookRequestDto;
import com.bookstore.dto.book.BookResponseDto;
import com.bookstore.dto.book.BookResponseDtoWithoutCategoryIds;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    List<BookResponseDto> findAll(Pageable pageable);

    BookResponseDto getById(Long id);

    BookResponseDto save(BookRequestDto requestDto);

    BookResponseDto update(Long id, BookRequestDto bookRequestDto);

    void deleteById(Long id);

    List<BookResponseDtoWithoutCategoryIds> findAllByCategoryId(Pageable pageable, Long categoryId);
}

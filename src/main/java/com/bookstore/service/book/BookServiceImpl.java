package com.bookstore.service.book;

import com.bookstore.dto.book.BookRequestDto;
import com.bookstore.dto.book.BookResponseDto;
import com.bookstore.dto.book.BookResponseDtoWithoutCategoryIds;
import com.bookstore.exception.EntityNotFoundException;
import com.bookstore.exception.InvalidUpdateDataException;
import com.bookstore.mapper.BookMapper;
import com.bookstore.model.Book;
import com.bookstore.repository.BookRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public List<BookResponseDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream().map(bookMapper::toDto).toList();
    }

    @Override
    public BookResponseDto getById(Long id) {
        return bookMapper.toDto(bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find book by id: " + id)));
    }

    @Override
    public BookResponseDto save(BookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        bookMapper.setCategoryToModel(requestDto, book);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public BookResponseDto update(Long id, BookRequestDto bookRequestDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't update because there is no "
                        + "book in the DB with id: " + id));
        if (bookRequestDto == null || !isUpdateDataPresent(bookRequestDto)) {
            throw new InvalidUpdateDataException("Can't update because the request is empty "
                    + "or the data is invalid");
        }
        if (bookRequestDto.getIsbn() != null) {
            book.setIsbn(bookRequestDto.getIsbn());
        }
        if (bookRequestDto.getAuthor() != null) {
            book.setAuthor(bookRequestDto.getAuthor());
        }
        if (bookRequestDto.getPrice() != null) {
            book.setPrice(bookRequestDto.getPrice());
        }
        if (bookRequestDto.getDescription() != null) {
            book.setDescription(bookRequestDto.getDescription());
        }
        if (bookRequestDto.getCoverImage() != null) {
            book.setCoverImage(bookRequestDto.getCoverImage());
        }
        if (bookRequestDto.getCategoryIds() != null) {
            bookMapper.setCategoryToModel(bookRequestDto, book);
        }
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookResponseDtoWithoutCategoryIds> findAllByCategoryId(
            Pageable pageable, Long categoryId) {
        return bookRepository.findAllByCategoryId(pageable, categoryId).stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }

    private boolean isUpdateDataPresent(BookRequestDto bookRequestDto) {
        return (bookRequestDto.getTitle() != null
                || bookRequestDto.getAuthor() != null
                || bookRequestDto.getDescription() != null
                || bookRequestDto.getIsbn() != null
                || bookRequestDto.getPrice() != null
                || bookRequestDto.getCoverImage() != null
                || bookRequestDto.getCategoryIds() != null);
    }
}

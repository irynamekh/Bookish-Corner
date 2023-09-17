package com.bookstore.mapper;

import com.bookstore.config.MapperConfig;
import com.bookstore.dto.book.BookRequestDto;
import com.bookstore.dto.book.BookResponseDto;
import com.bookstore.dto.book.BookResponseDtoWithoutCategoryIds;
import com.bookstore.model.Book;
import com.bookstore.model.Category;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    @Mapping(target = "categoryIds", ignore = true)
    BookResponseDto toDto(Book book);

    @Mapping(target = "categories", ignore = true)
    Book toModel(BookRequestDto requestDto);

    BookResponseDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIdsToDto(@MappingTarget BookResponseDto bookResponseDto, Book book) {
        if (book.getCategories() != null) {
            bookResponseDto.setCategoryIds(book.getCategories().stream()
                    .map(Category::getId)
                    .collect(Collectors.toSet()));
        }
    }

    @AfterMapping
    default void setCategoryToModel(@MappingTarget BookRequestDto requestDto, Book book) {
        Set<Category> categories = getCategoriesById(requestDto.getCategoryIds());
        book.setCategories(categories);
    }

    private Set<Category> getCategoriesById(Set<Long> categoryIds) {
        return categoryIds.stream()
                .map(l -> {
                    Category category = new Category();
                    category.setId(l);
                    return category;
                })
                .collect(Collectors.toSet());
    }
}

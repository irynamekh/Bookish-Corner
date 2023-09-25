package com.bookstore.mapper;

import com.bookstore.config.MapperConfig;
import com.bookstore.dto.category.CategoryRequestDto;
import com.bookstore.dto.category.CategoryResponseDto;
import com.bookstore.model.Category;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryResponseDto toDto(Category category);

    Category toModel(CategoryRequestDto requestDto);
}

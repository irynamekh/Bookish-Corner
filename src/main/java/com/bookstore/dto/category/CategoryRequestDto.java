package com.bookstore.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryRequestDto {
    @NotNull
    @NotBlank
    @Size(min = 1, max = 255)
    private String name;
    @NotNull
    @NotBlank
    @Size(min = 1, max = 255)
    private String description;
}

package com.bookstore.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.URL;

@Data
@Accessors(chain = true)
public class BookRequestDto {
    @NotNull
    @NotBlank
    @Size(min = 1, max = 255)
    private String title;
    @NotNull
    @NotBlank
    @Size(min = 1, max = 255)
    private String author;
    @NotNull
    @NotBlank
    @ISBN(message = "isbn value shoud be unique")
    private String isbn;
    @NotNull
    @Min(0)
    private BigDecimal price;
    private String description;
    @URL
    private String coverImage;
    private Set<Long> categoryIds;
}

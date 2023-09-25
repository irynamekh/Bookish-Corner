package com.bookstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginRequestDto {
    @NotNull
    @Email
    private String email;
    @NotNull
    @NotBlank
    @Size(min = 8, max = 30)
    private String password;
}

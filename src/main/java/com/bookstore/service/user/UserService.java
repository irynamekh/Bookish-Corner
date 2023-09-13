package com.bookstore.service.user;

import com.bookstore.dto.user.UserRegistrationRequestDto;
import com.bookstore.dto.user.UserResponseDto;
import com.bookstore.exception.RegistrationException;

public interface UserService {
    public UserResponseDto register(UserRegistrationRequestDto userRequestDto)
            throws RegistrationException;
}

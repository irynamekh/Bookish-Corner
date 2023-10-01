package com.bookstore.service.user;

import com.bookstore.dto.user.UserRegistrationRequestDto;
import com.bookstore.dto.user.UserResponseDto;
import com.bookstore.exception.RegistrationException;
import com.bookstore.model.User;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto userRequestDto)
            throws RegistrationException;

    User getUser(Long userId);
}

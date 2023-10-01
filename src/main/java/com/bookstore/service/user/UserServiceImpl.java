package com.bookstore.service.user;

import com.bookstore.dto.user.UserRegistrationRequestDto;
import com.bookstore.dto.user.UserResponseDto;
import com.bookstore.exception.EntityNotFoundException;
import com.bookstore.exception.RegistrationException;
import com.bookstore.mapper.UserMapper;
import com.bookstore.model.Role;
import com.bookstore.model.User;
import com.bookstore.repository.RoleRepository;
import com.bookstore.repository.UserRepository;
import com.bookstore.service.cart.ShoppingCartService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final ShoppingCartService shoppingCartService;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto userRequestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(userRequestDto.getEmail()).isPresent()) {
            throw new RegistrationException("User with such an email " + userRequestDto.getEmail()
                    + "already exists in the system.");
        }
        User user = userMapper.toModel(userRequestDto);
        Role userRole = roleRepository.getByRoleName(Role.RoleName.USER);
        user.setRoles(Set.of(userRole));
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        User savedUser = userRepository.save(user);
        shoppingCartService.createCart(savedUser);
        return userMapper.toDto(savedUser);
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find user by id: " + userId));
    }
}

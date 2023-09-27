package com.bookstore.controller;

import com.bookstore.dto.order.CreateOrderRequestDto;
import com.bookstore.dto.order.OrderResponseDto;
import com.bookstore.dto.order.UpdateStatusRequestDto;
import com.bookstore.dto.orderitem.OrderItemResponseDto;
import com.bookstore.model.User;
import com.bookstore.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User's orders management", description = "Endpoints for managing user's orders")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "Get all orders",
            description = "Retrieve user's order history")
    public List<OrderResponseDto> getAll(Pageable pageable, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getAll(pageable, user.getId());
    }

    @GetMapping("/{id}/items")
    @Operation(summary = "Get all order items",
            description = "Retrieve all OrderItems for a specific order")
    public List<OrderItemResponseDto> getAll(@PathVariable Long id, Pageable pageable,
                                             Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getAllByOrderId(id, pageable, user.getId());
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Get order item by id",
            description = "Retrieve a specific OrderItem within an order by item id")
    public OrderItemResponseDto getOrderItem(@PathVariable Long orderId,
                                             @PathVariable Long itemId,
                                             Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrderItemById(orderId, itemId, user.getId());
    }

    @PostMapping
    @Operation(summary = "Place an order",
            description = "Create a new order")
    public OrderResponseDto createOrder(
            @RequestBody @Valid CreateOrderRequestDto requestDto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.save(requestDto, user.getId());
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Change order status",
            description = "Update order status for user's order")
    public OrderResponseDto updateOrderStatus(@PathVariable Long id,
                                      @RequestBody @Valid UpdateStatusRequestDto requestDto) {
        return orderService.updateOrderStatus(id, requestDto);
    }
}

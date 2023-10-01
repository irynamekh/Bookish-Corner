package com.bookstore.service.order;

import com.bookstore.dto.order.CreateOrderRequestDto;
import com.bookstore.dto.order.OrderResponseDto;
import com.bookstore.dto.order.UpdateStatusRequestDto;
import com.bookstore.dto.orderitem.OrderItemResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    List<OrderResponseDto> getAll(Pageable pageable, Long userId);

    OrderResponseDto save(CreateOrderRequestDto requestDto, Long userId);

    OrderResponseDto updateOrderStatus(Long id, UpdateStatusRequestDto requestDto);

    List<OrderItemResponseDto> getAllByOrderId(Long orderId, Pageable pageable, Long userId);

    OrderItemResponseDto getOrderItemById(Long orderId, Long itemId);
}

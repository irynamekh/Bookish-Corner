package com.bookstore.service.order;

import com.bookstore.dto.order.CreateOrderRequestDto;
import com.bookstore.dto.order.OrderResponseDto;
import com.bookstore.dto.order.UpdateStatusRequestDto;
import com.bookstore.dto.orderitem.OrderItemResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    List<OrderResponseDto> findAll(Pageable pageable);

    OrderResponseDto save(CreateOrderRequestDto requestDto);

    OrderResponseDto update(Long id, UpdateStatusRequestDto requestDto);

    List<OrderItemResponseDto> findAllByOrderId(Long orderId, Pageable pageable);

    OrderItemResponseDto findOrderItemById(Long orderId, Long itemId);
}

package com.bookstore.service.order;

import com.bookstore.dto.order.CreateOrderRequestDto;
import com.bookstore.dto.order.OrderResponseDto;
import com.bookstore.dto.order.UpdateStatusRequestDto;
import com.bookstore.dto.orderitem.OrderItemResponseDto;
import com.bookstore.exception.EntityNotFoundException;
import com.bookstore.mapper.OrderItemMapper;
import com.bookstore.mapper.OrderMapper;
import com.bookstore.model.Order;
import com.bookstore.model.OrderItem;
import com.bookstore.model.ShoppingCart;
import com.bookstore.model.User;
import com.bookstore.repository.OrderItemRepository;
import com.bookstore.repository.OrderRepository;
import com.bookstore.service.cart.ShoppingCartService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartService shoppingCartService;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    public List<OrderResponseDto> findAll(Pageable pageable) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<OrderItemResponseDto> orderItems =
                orderRepository.findAllByUserId(user.getId()).stream()
                .map(Order::getOrderItems)
                .flatMap(Collection::stream)
                .map(orderItemMapper::toDto)
                .collect(Collectors.toSet());
        return orderRepository.findAllByUserId(user.getId()).stream()
                .map(orderMapper::toDto)
                .peek(c -> c.setOrderItems(orderItems))
                .toList();
    }

    @Override
    public OrderResponseDto save(CreateOrderRequestDto requestDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ShoppingCart cart = shoppingCartService.getById(user.getId());

        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(requestDto.getShippingAddress());
        order.setOrderDate(LocalDateTime.now());
        order.setTotal(cart.getCartItems().stream()
                .map(c -> c.getBook().getPrice().multiply(BigDecimal.valueOf(c.getQuantity())))
                .reduce(BigDecimal.valueOf(0), BigDecimal::add));
        orderRepository.save(order);

        Set<OrderItem> orderItems = cart.getCartItems().stream()
                .map(orderItemMapper::fromCartItem)
                .peek(oi -> oi.setPrice(oi.getPrice().multiply(
                        BigDecimal.valueOf(oi.getQuantity()))))
                .peek(oi -> oi.setOrder(order))
                .peek(orderItemRepository::save)
                .collect(Collectors.toSet());
        Set<OrderItemResponseDto> cartItems = orderItems.stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toSet());

        OrderResponseDto dto = orderMapper.toDto(order);
        dto.setOrderItems(cartItems);
        return dto;
    }

    @Override
    public OrderResponseDto update(Long id, UpdateStatusRequestDto requestDto) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                "Can't find order by id: " + id));
        order.setStatus(requestDto.getStatus());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderItemResponseDto> findAllByOrderId(Long orderId, Pageable pageable) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Order order = orderRepository.findAllByUserId(user.getId()).stream()
                .filter(o -> Objects.equals(o.getId(), orderId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find order by id: " + orderId));
        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemResponseDto findOrderItemById(Long orderId, Long itemId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Order order = orderRepository.findAllByUserId(user.getId()).stream()
                .filter(o -> Objects.equals(o.getId(), orderId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find order by id: " + orderId));
        return order.getOrderItems().stream()
                .filter(c -> Objects.equals(c.getId(), itemId))
                .map(orderItemMapper::toDto)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find order item by id: " + itemId));
    }
}

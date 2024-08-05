package com.order.api.service;


import com.order.api.auth.entity.User;
import com.order.api.auth.repository.UserRepository;
import com.order.api.auth.service.AuthorizationService;
import com.order.api.dto.request.OrderItemRequestDTO;
import com.order.api.dto.request.OrderRequestDTO;
import com.order.api.dto.response.OrderItemResponseDTO;
import com.order.api.dto.response.OrderResponseDTO;
import com.order.api.entity.OrderEntity;
import com.order.api.entity.OrderItem;
import com.order.api.entity.Product;
import com.order.api.exceptions.InvalidQuantityException;
import com.order.api.exceptions.ResourceNotFoundException;
import com.order.api.repository.OrderRepository;
import com.order.api.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = (User) userRepository.findByLogin(username);

        List<OrderItem> items = new ArrayList<>();

        BigDecimal total = BigDecimal.ZERO;
        int itemTotal = 0;

        OrderEntity order = new OrderEntity();
        order.setUser(user);

        for (OrderItemRequestDTO itemDTO : orderRequestDTO.items()) {
            if (itemDTO.quantity() <= 0) {
                throw new InvalidQuantityException("Quantity must be greater than zero");
            }
            Product product = productRepository.findById(itemDTO.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            // Crie o item do pedido com a referência ao pedido
            OrderItem orderItem = new OrderItem(product, itemDTO.quantity(), order);
            items.add(orderItem);
            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(itemDTO.quantity())));
            itemTotal += itemDTO.quantity();
        }

        order.setTotal(total);
        order.setItemTotal(itemTotal);
        order.setItems(items);
        OrderEntity savedOrder = orderRepository.save(order);
        return convertToResponseDTO(savedOrder);
    }

    public OrderResponseDTO getOrderById(Long id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return convertToResponseDTO(order);
    }

    public List<OrderResponseDTO> getAllOrders() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = (User) userRepository.findByLogin(username);
        List<OrderEntity> orders = orderRepository.findByUser(user);
        return orders.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public OrderResponseDTO updateOrder(Long id, OrderRequestDTO orderRequestDTO) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        int itemTotal = 0;

        for (OrderItemRequestDTO itemDTO : orderRequestDTO.items()) {
            if (itemDTO.quantity() <= 0) {
                throw new InvalidQuantityException("Quantity must be greater than zero");
            }
            Product product = productRepository.findById(itemDTO.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            OrderItem orderItem = new OrderItem(product, itemDTO.quantity(), order);
            items.add(orderItem);
            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(itemDTO.quantity())));
            itemTotal += itemDTO.quantity();
        }

        order.setTotal(total);
        order.setItemTotal(itemTotal);
        order.setItems(items);
        OrderEntity updatedOrder = orderRepository.save(order);
        return convertToResponseDTO(updatedOrder);
    }

    public void deleteOrder(Long id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        orderRepository.delete(order);
    }

    private OrderResponseDTO convertToResponseDTO(OrderEntity order) {
        List<OrderItemResponseDTO> itemDTOs = order.getItems().stream()
                .map(item -> new OrderItemResponseDTO(
                        item.getId(),
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getProduct().getDescription(),
                        item.getProduct().getBrand(),
                        item.getProduct().getCategory(),
                        item.getProduct().getPrice(),
                        item.getQuantity()
                ))
                .collect(Collectors.toList());
        return new OrderResponseDTO(order.getOrderId(), order.getTotal(), order.getItemTotal(), itemDTOs);
    }
}


package com.order.api.service;


import com.order.api.dto.response.OrderItemResponseDTO;
import com.order.api.entity.OrderItem;
import com.order.api.exceptions.ResourceNotFoundException;
import com.order.api.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    public OrderItemResponseDTO getOrderItemById(Long id) {
        OrderItem item = orderItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found"));
        return new OrderItemResponseDTO(item.getId(), item.getProduct().getId(),
                item.getProduct().getName(), item.getProduct().getPrice(), item.getQuantity());
    }
}

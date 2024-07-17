package com.order.api.dto;

import com.order.api.entity.Product;

import java.math.BigDecimal;

public record OrderItemDTO(
        Long id,
        Product product,
        Integer quantity
) {}

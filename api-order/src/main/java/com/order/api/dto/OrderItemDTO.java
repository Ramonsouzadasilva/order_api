package com.order.api.dto;

import java.math.BigDecimal;

public record OrderItemDTO(
        Long id,
        String product,
        Integer quantity,
        BigDecimal price
) {}

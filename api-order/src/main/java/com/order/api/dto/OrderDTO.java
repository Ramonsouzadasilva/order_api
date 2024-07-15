package com.order.api.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderDTO(
        Long id,
        BigDecimal total,
        List<OrderItemDTO> items
) {}

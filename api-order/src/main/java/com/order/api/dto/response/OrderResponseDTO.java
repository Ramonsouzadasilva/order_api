package com.order.api.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponseDTO(Long id, BigDecimal total, Integer itemTotal, List<OrderItemResponseDTO> items) {}
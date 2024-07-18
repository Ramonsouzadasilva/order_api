package com.order.api.dto.response;

import java.math.BigDecimal;

public record OrderItemResponseDTO(Long id, Long productId, String productName, BigDecimal price, Integer quantity) {}

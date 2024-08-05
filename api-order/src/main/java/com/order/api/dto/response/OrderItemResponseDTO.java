package com.order.api.dto.response;

import java.math.BigDecimal;

public record OrderItemResponseDTO(Long id, Long productId, String productName, String productDescription,
                                   String productBrand, String productCategory,
                                   BigDecimal price, Integer quantity) {}

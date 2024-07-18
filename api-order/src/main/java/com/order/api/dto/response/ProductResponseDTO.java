package com.order.api.dto.response;

import java.math.BigDecimal;

public record ProductResponseDTO(Long id, String name, String description, String linkImage, BigDecimal price, Integer inventory, String brand, String category,
                                 String productCode) {}

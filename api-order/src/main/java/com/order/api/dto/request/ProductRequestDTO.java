package com.order.api.dto.request;

import java.math.BigDecimal;

public record ProductRequestDTO(String name, String description, String linkImage, BigDecimal price, Integer inventory,
                                String brand, String category, String productCode) {}

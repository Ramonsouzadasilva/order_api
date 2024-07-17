package com.order.api.dto;

import java.math.BigDecimal;

public record ProductDTO(
        Long id,
        String name,
        String description,
        String linkImage,
        BigDecimal price,
        Integer inventory,
        String brand,
        String category,
        String productCode
) {
    // Você pode adicionar métodos adicionais aqui, se necessário
}
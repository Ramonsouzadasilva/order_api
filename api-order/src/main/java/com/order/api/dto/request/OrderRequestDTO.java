package com.order.api.dto.request;

import java.util.List;

public record OrderRequestDTO(List<OrderItemRequestDTO> items) {}

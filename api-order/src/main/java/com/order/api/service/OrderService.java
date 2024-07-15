package com.order.api.service;

import com.order.api.dto.OrderDTO;
import com.order.api.dto.OrderItemDTO;
import com.order.api.entity.OrderEntity;
import com.order.api.entity.OrderItem;
import com.order.api.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void save(OrderDTO orderDTO) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setTotal(calculateTotal(orderDTO.items()));
        orderEntity.setItems(convertToOrderItems(orderDTO.items()));
        orderRepository.save(orderEntity);
    }

    public List<OrderDTO> findAll() {
        return orderRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> findOrdersByProduct(String product) {
        return orderRepository.findByProductLike(product).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<OrderDTO> findById(Long id) {
        return orderRepository.findById(id).map(this::toDTO);
    }

    public void update(OrderDTO orderDTO) {
        OrderEntity orderEntity = orderRepository.findById(orderDTO.id())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        orderEntity.setTotal(calculateTotal(orderDTO.items()));
        orderEntity.setItems(convertToOrderItems(orderDTO.items()));
        orderRepository.save(orderEntity);
    }

    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    private BigDecimal calculateTotal(List<OrderItemDTO> items) {
        return items.stream()
                .map(i -> i.price().multiply(BigDecimal.valueOf(i.quantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private List<OrderItem> convertToOrderItems(List<OrderItemDTO> items) {
        return items.stream()
                .map(i -> new OrderItem(i.product(), i.quantity(), i.price()))
                .collect(Collectors.toList());
    }

    private OrderDTO toDTO(OrderEntity orderEntity) {
        List<OrderItemDTO> items = orderEntity.getItems().stream()
                .map(item -> new OrderItemDTO(item.getItemId(), item.getProduct(), item.getQuantity(), item.getPrice()))
                .collect(Collectors.toList());
        return new OrderDTO(orderEntity.getOrderId(), orderEntity.getTotal(), items);
    }
}

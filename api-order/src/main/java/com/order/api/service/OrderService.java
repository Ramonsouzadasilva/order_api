package com.order.api.service;

import com.order.api.dto.OrderDTO;
import com.order.api.dto.OrderItemDTO;
import com.order.api.entity.OrderEntity;
import com.order.api.entity.OrderItem;
import com.order.api.entity.Product;
import com.order.api.repository.OrderRepository;
import com.order.api.repository.ProductRepository;
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

    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository,  ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
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

//    public List<OrderDTO> findOrdersByProduct(Product product) {
//        return orderRepository.findByProductLike(product).stream()
//                .map(this::toDTO)
//                .collect(Collectors.toList());
//    }

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
                .map(i -> {
                    BigDecimal price = i.product().getPrice(); // Accessing the price from DTO
                    Integer quantity = i.quantity();
                    return price.multiply(BigDecimal.valueOf(quantity));
                })
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private List<OrderItem> convertToOrderItems(List<OrderItemDTO> items) {
        return items.stream()
                .map(i -> {
                    Product product = productRepository.findById(i.product().getId())
                            .orElseThrow(() -> new IllegalArgumentException("Product not found"));
                    return new OrderItem(product, i.quantity());
                })
                .collect(Collectors.toList());
    }

    private OrderDTO toDTO(OrderEntity orderEntity) {
        List<OrderItemDTO> items = orderEntity.getItems().stream()
                .map(item -> new OrderItemDTO(item.getId(), item.getProduct(), item.getQuantity()))
                .collect(Collectors.toList());
        return new OrderDTO(orderEntity.getOrderId(), orderEntity.getTotal(), items);
    }
}

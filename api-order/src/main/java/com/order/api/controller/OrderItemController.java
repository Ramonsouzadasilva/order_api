package com.order.api.controller;

import com.order.api.dto.OrderItemDTO;
import com.order.api.entity.OrderItem;
import com.order.api.service.OrderItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/order-items")
public class OrderItemController {
    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @GetMapping
    public List<OrderItemDTO> getAll() {
        return orderItemService.findAll().stream()
                .map(item -> new OrderItemDTO(item.getItemId(), item.getProduct(), item.getQuantity(), item.getPrice()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItemDTO> getById(@PathVariable Long id) {
        return orderItemService.findById(id)
                .map(item -> ResponseEntity.ok(new OrderItemDTO(item.getItemId(), item.getProduct(), item.getQuantity(), item.getPrice())))
                .orElse(ResponseEntity.notFound().build());
    }

//    @PostMapping
//    public ResponseEntity<OrderItemDTO> create(@RequestBody OrderItemDTO orderItemDTO) {
//        OrderItem orderItem = new OrderItem(orderItemDTO.product(), orderItemDTO.quantity(), orderItemDTO.price());
//        orderItemService.save(orderItem);
//        return ResponseEntity.ok(new OrderItemDTO(orderItem.getItemId(), orderItem.getProduct(), orderItem.getQuantity(), orderItem.getPrice()));
//    }

//    @PutMapping("/{id}")
//    public ResponseEntity<OrderItemDTO> update(@PathVariable Long id, @RequestBody OrderItemDTO orderItemDTO) {
//        return orderItemService.findById(id)
//                .map(existingItem -> {
//                    existingItem.setProduct(orderItemDTO.product());
//                    existingItem.setQuantity(orderItemDTO.quantity());
//                    existingItem.setPrice(orderItemDTO.price());
//                    orderItemService.save(existingItem);
//                    return ResponseEntity.ok(new OrderItemDTO(existingItem.getItemId(), existingItem.getProduct(), existingItem.getQuantity(), existingItem.getPrice()));
//                })
//                .orElse(ResponseEntity.notFound().build());
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (orderItemService.findById(id).isPresent()) {
            orderItemService.delete(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

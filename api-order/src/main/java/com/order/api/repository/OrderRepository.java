package com.order.api.repository;

import com.order.api.entity.OrderEntity;
import com.order.api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
//    @Query("SELECT DISTINCT order FROM OrderEntity order JOIN order.items item WHERE item.product LIKE %:name%")
//    List<OrderEntity> findByProductLike(Product product);

}

package com.order.api.repository;

import com.order.api.auth.entity.User;
import com.order.api.entity.OrderEntity;
import com.order.api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

        List<OrderEntity> findByUser(User user);
}


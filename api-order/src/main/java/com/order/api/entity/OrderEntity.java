package com.order.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Table(name = "orders")
@Entity
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total")
    private BigDecimal total;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    // Construtor vazio (necessário para JPA)
    public OrderEntity() {
    }

    // Construtor com dados
    public OrderEntity(BigDecimal total, List<OrderItem> items) {
        this.total = total;
        this.items = items;
        // Configurar o relacionamento bidirecional
        setItems(items);
    }

    // Getters e Setters

    public Long getOrderId() {
        return id;
    }

    public void setOrderId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items.clear();
        if (items != null) {
            this.items.addAll(items);
            this.items.forEach(item -> item.setOrder(this));
        }
    }
}


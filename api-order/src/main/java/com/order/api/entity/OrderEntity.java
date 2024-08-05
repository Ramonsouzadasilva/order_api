package com.order.api.entity;

import com.order.api.auth.entity.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Table(name = "orders")
@Entity
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_total", nullable = false)
    private Integer itemTotal;

    @Column(name = "total", nullable = false)
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    // Construtor vazio (necessário para JPA)
    public OrderEntity() {
    }

    // Construtor com dados
    public OrderEntity(BigDecimal total, User user, Integer itemTotal, List<OrderItem> items) {
        this.total = total;
        this.items = items;
        this.itemTotal = itemTotal;
        // Configurar o relacionamento bidirecional
        setItems(items);
        this.user = user;
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

    public Integer getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(Integer itemTotal) {
        this.itemTotal = itemTotal;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}


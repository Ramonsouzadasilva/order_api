package com.order.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Table(name="products")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name= "description", nullable = false, length = 200)
    private String description;

    @Column(name = "linkImage", nullable = false)
    private String linkImage;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "inventory", nullable = false)
    private Integer inventory;

    @Column(name = "brand", nullable = false, length = 50)
    private String brand;

    @Column(name = "category", nullable = false, length = 50)
    private String category;

    @Column(name = "product_code", nullable = false, length = 6 )
    private String productCode;

}

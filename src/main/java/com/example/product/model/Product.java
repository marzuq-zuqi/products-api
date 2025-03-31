package com.example.product.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.processing.Generated;

@Getter
@Setter
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 50, message="Product name must be between 2 and 10 characters")
    @Column(unique = true, nullable = false)
    private String name;

    @NotNull(message="Price is required")
    @Positive(message="Price must be greater than zero")
    private double price;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 0, message = "Quantity must be at least 0")
    @Column(nullable = false)
    private int quantity;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

//    public Product(String name, double price, int quantity, String description) {
//        this.name = name;
//        this.price = price;
//        this.quantity = quantity;
//        this.description = description;
//    }
}

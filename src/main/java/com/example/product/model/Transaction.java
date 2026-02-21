package com.example.product.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime transactionDate;
    private double totalAmount;
    private String paymentMethod;

    @ManyToOne
    private Customer customer;

    @OneToMany
    private List<TransactionItem> items = new ArrayList<>();
}

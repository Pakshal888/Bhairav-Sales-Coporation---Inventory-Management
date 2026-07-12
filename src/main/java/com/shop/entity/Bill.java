package com.shop.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private LocalDateTime date;
    private double totalAmount;
    private double discount;
    private double finalAmount;

    @ElementCollection
    private List<String> items; // Simple list of item descriptions

    @ElementCollection
    private List<String> companies;

    @ElementCollection
    private List<Double> prices;

    @ElementCollection
    private List<Integer> quantities;
}
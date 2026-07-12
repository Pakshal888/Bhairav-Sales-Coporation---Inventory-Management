package com.shop.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Estimate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private LocalDateTime date;
    private double totalAmount;
    private double discount;
    private double finalAmount;
    private String category;

    @ElementCollection
    private List<String> items;

    @ElementCollection
    private List<String> companies;

    @ElementCollection
    private List<Double> prices;

    @ElementCollection
    private List<Integer> quantities;
}
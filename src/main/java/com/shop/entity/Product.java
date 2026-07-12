
package com.shop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;
    private String size;
    private double purchaseRate;
    private double stockRate;
    private int thickness;
    private int quantity;
    private String category; // Hardware, Electric, Plumbing, Fancyware, Sanitaryware

    public double getPrice(){
        return stockRate * thickness;
    }
}

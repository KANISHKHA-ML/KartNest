package com.cts.entity;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "productId") 
    private Products products;

    private int quantity;
    
    public double getTotalPrice() {
       
        return products.getPrice() * quantity;
    }
}
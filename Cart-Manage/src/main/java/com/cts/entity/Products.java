package com.cts.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "products") 
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) 
    private Long productId; 
    private String productName;
    private String description;
    private double price;
    private int numberOfStock;

    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cart> cartItems;   
}
package com.cts.service;

import java.util.List;
import com.cts.entity.Products;

public interface ProductService {
    List<Products> getAllProducts();
    List<com.cts.entity.Products> findByProductNameLike(String name);
    void save(Products product);
    boolean delete(Long id);
    boolean update(Long id, Products updatedProduct);
	Products getProductById(Long id);
}

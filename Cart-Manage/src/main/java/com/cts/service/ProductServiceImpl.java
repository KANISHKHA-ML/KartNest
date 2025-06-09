package com.cts.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.entity.Products;
import com.cts.repository.ProductRepo;



@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepo repo;

    @Override
    public List<Products> getAllProducts() {
        return repo.findAll();
    }

    @Override
    public List<Products> findByProductNameLike(String name) {
        return repo.findByProductNameLike(name);
    }

    @Override
    public void save(Products product) {
        repo.save(product);
    }
    
    @Override
    public boolean delete(Long id){
        if (repo.existsById(id)) {
        	repo.deleteById(id);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean update(Long id, Products updatedProduct) {
        if (repo.existsById(id)) {
            updatedProduct.setProductId(id); 
            repo.save(updatedProduct);
            return true;
        }
        return false;
    }
    
    @Override
    public Products getProductById(Long id) {
        return repo.findById(id).orElse(null);
    }

}

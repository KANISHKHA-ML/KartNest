package com.cts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//import com.cts.entity.Product;
import com.cts.entity.Products;
@Repository
public interface ProductRepo extends JpaRepository<Products, Long> {
    List<Products> findByProductNameLike(String productName);

}
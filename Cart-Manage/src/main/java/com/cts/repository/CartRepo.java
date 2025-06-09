package com.cts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.entity.Cart;

import java.util.List;
@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {
   
	Cart findByUserUserIdAndProductsProductId(Long userId, Long productsId);
    List<Cart> findByUserUserId(Long userId);
	
}


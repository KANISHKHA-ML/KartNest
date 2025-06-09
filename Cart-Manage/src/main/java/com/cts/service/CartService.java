package com.cts.service;

import java.util.List;

import com.cts.entity.Cart;
//import com.cts.entity.Product;
import com.cts.entity.Products;


public interface CartService {
    String addItemToCart(Long userId, Long productId, int quantity);
    List<Cart> getUserCart(Long userId);
    String removeItem(Long userId, Long productId);
    List<Products> getAllProducts();
	void updateQuantity(Long userId, Long productId, int quantity);
	double calculateTotal(Long userId);
	Products getProductById(Long productId);
	void clearCartByUserId(Long userId);

}

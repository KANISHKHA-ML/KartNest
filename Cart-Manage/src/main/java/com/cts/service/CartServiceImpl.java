package com.cts.service;

import com.cts.entity.Cart;
import com.cts.entity.Products;
import com.cts.entity.User;
import com.cts.repository.CartRepo;
import com.cts.repository.ProductRepo;
import com.cts.repository.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserRepo userRepo;
    
   
    public double calculateTotal(Long userId) {
        List<Cart> cartItems = cartRepo.findByUserUserId(userId);
        return cartItems.stream()
                        .mapToDouble(Cart::getTotalPrice)
                        .sum();
    }

    @Override
    public String addItemToCart(Long userId, Long productId, int quantity) {
        User user = userRepo.findById(userId).orElse(null);
        Products product = productRepo.findById(productId).orElse(null);

        if (user == null || product == null) return "Invalid User or Product";
        Cart existing = cartRepo.findByUserUserIdAndProductsProductId(userId, productId);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
            cartRepo.save(existing);
        } else {
            Cart cart = new Cart();
            cart.setUser(user);
            cart.setProducts(product);
            cart.setQuantity(quantity);
            cartRepo.save(cart);
        }
        return "Product added to cart";
    }

    @Override
    public List<Cart> getUserCart(Long userId) {
        return cartRepo.findByUserUserId(userId);
    }

    @Override
    public String removeItem(Long userId, Long productId) {
        Cart cart = cartRepo.findByUserUserIdAndProductsProductId(userId, productId);
        if (cart != null) {
            cartRepo.delete(cart);
            return "Removed Successfully";
        }
        return "Item not found";
    }

    @Override
    public List<Products> getAllProducts() {
        return productRepo.findAll();
    }

    @Override
    public void updateQuantity(Long userId, Long productId, int quantity) {
        Cart cart = cartRepo.findByUserUserIdAndProductsProductId(userId, productId);
        if (cart != null) {
            if (quantity <= 0) {
                cartRepo.delete(cart);
            } else {
                cart.setQuantity(quantity);
                cartRepo.save(cart);
            }
        }
    }

    @Override
    public Products getProductById(Long productId) {
        return productRepo.findById(productId).orElse(null);
    }
    
    @Override
    public void clearCartByUserId(Long userId) {
        List<Cart> cartItems = cartRepo.findByUserUserId(userId);
        cartRepo.deleteAll(cartItems);
    }

		
	}


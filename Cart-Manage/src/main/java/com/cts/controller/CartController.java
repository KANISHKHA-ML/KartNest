package com.cts.controller;

import com.cts.entity.Cart;
import com.cts.entity.User;
import com.cts.service.CartService;
import com.cts.service.UserService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@SuppressWarnings("unchecked")
@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private CartService cartService;

    @GetMapping("/products")
    public String listProducts(Model model) {
        model.addAttribute("products", cartService.getAllProducts());
        model.addAttribute("cartItem", new Cart());
        return "displayProduct";
    }

    @PostMapping("/add")
    public String addToCart(@ModelAttribute Cart cartItem, HttpSession session, RedirectAttributes redirectAttributes) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userService.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + currentUsername));
        Long userId = user.getUserId();

        cartService.addItemToCart(userId, cartItem.getProducts().getProductId(), cartItem.getQuantity());

        redirectAttributes.addFlashAttribute("message", "Product added to cart");
        return "redirect:/cart/products";
    }


    @GetMapping("/view")
    public String viewCart(Model model, HttpSession session) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found!"));
        Long userId = user.getUserId();

        List<Cart> cartItems = cartService.getUserCart(userId);
        double grandTotal = cartService.calculateTotal(userId);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("grandTotal", grandTotal);
        session.setAttribute("userID", userId);
        return "prdCart";
    }

    @PostMapping("/update")
    public String updateQuantity(@RequestParam Long productId, @RequestParam int quantity, HttpSession session) {
        Long userId = (Long) session.getAttribute("userID");
        cartService.updateQuantity(userId, productId, quantity);
        return "redirect:/cart/view";
    }

    @PostMapping("/delete")
    public String deleteItem(@RequestParam Long productId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userID");
        cartService.removeItem(userId, productId);
        return "redirect:/cart/view";
    }
}

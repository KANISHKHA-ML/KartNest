package com.cts.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cts.entity.Order;
import com.cts.entity.Order.OrderStatus;
import com.cts.service.CartService;
import com.cts.service.OrderService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/order")
public class OrderController {

@Autowired
private OrderService OrderSer;

@Autowired
    private CartService cartService;

@GetMapping("/placeOrder")
public String placeOrder(@RequestParam Long userId, HttpSession session, Model model) {
    
    double updatedTotalAmount = cartService.calculateTotal(userId);
    Order existingOrder = (Order) session.getAttribute("order");

    if (existingOrder != null) {
    
        existingOrder.setTotalAmount(updatedTotalAmount);
        OrderSer.updateOrder(existingOrder); 
        model.addAttribute("order", existingOrder);
        
    } else {
    
        Order order = OrderSer.createOrder(userId, updatedTotalAmount);
        session.setAttribute("order", order);
        model.addAttribute("order", order);
    }

    return "orderSummary";
}

@GetMapping("/errorPage")
public String ErrorPage() {
return "errorPage";
}

    @PostMapping("/confirmOrder")
    public String confirmOrder(@RequestParam Long orderId, Model model) {
        Order order = OrderSer.getOrderDetails(orderId);
        OrderSer.updateOrderStatus(orderId, OrderStatus.PENDING);
        model.addAttribute("order", order);
        
        return "paymentOptions";
    }

    @PostMapping("/processPayment")
    public String processPayment(@RequestParam Long orderId, @RequestParam String paymentMode, Model model, HttpSession session) {
        
    Long userId = (Long) session.getAttribute("userID");
    
    Order order = OrderSer.getOrderDetails(orderId);

        if (order == null) {
            model.addAttribute("error", "Order not found.");
            return "/order/errorPage";
        }

        OrderSer.updateOrderStatus(orderId, OrderStatus.SHIPPED);

        model.addAttribute("message", "Your order is confirmed!");
        model.addAttribute("order", order);

        cartService.clearCartByUserId(userId);
        
        return "orderConfirmation";
    }
    
    @GetMapping("/myOrders")
    public String showMyOrders(@RequestParam Long userId, Model model) {
        List<Order> userOrders = OrderSer.getOrdersByUserId(userId);
        model.addAttribute("userOrders", userOrders);
        return "myOrders";
    }

    @PostMapping("/admin/updateStatus")
    public String updateOrderStatus(@RequestParam Long orderId, @RequestParam String status, HttpSession session) {
        Order order = OrderSer.getOrderDetails(orderId);
        
        if (order != null) {
            order.setStatus(OrderStatus.valueOf(status));
            OrderSer.updateOrder(order);
            session.setAttribute("order", OrderSer.getOrderDetails(orderId));
        }
        
        return "redirect:/admin/orders";
    }

}

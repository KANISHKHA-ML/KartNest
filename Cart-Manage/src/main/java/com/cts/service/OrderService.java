package com.cts.service;

import java.util.List;

import com.cts.entity.Order;
import com.cts.entity.Order.OrderStatus;

public interface OrderService {
	Order createOrder(Long userId, double totalAmount);
    void updateOrderStatus(Long orderId, OrderStatus status);
    Order getOrderDetails(Long orderId);
    Order updateOrder(Order order);
    List<Order> getOrdersByUserId(Long userId);
    List<Order> getAllOrders();
}

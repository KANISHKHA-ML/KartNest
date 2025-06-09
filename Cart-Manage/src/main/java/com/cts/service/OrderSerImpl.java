package com.cts.service;
 
import java.time.LocalDate;
import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import com.cts.entity.Order;
import com.cts.entity.Order.OrderStatus;
import com.cts.repository.OrderRepo;
 
 
@Service
public class OrderSerImpl implements OrderService  {
	@Autowired
    private OrderRepo orderRepository;
 
	@Override
    public Order createOrder(Long userId, double totalAmount) {
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setOrderDate(LocalDate.now());
        order.setStatus(OrderStatus.PENDING);
        return orderRepository.save(order);
    }
 
	@Override
	public Order getOrderDetails(Long orderId) {
	    return orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
	}
	@Override
	public Order updateOrder(Order order) {
	    return orderRepository.save(order);
	}
 
	@Override
	public void updateOrderStatus(Long orderId, OrderStatus newStatus) {
	    
	    orderRepository.findById(orderId).ifPresent(order -> {
	        order.setStatus(newStatus);
	        orderRepository.save(order);
	    });
 
	  
	}
 
    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
 
    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
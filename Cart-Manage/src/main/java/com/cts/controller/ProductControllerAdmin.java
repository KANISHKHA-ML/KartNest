package com.cts.controller;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.cts.entity.Order;
import com.cts.entity.Products;
import com.cts.entity.Order.OrderStatus;
import com.cts.service.OrderService;
import com.cts.service.ProductService;


@Controller
@RequestMapping("/admin")
public class ProductControllerAdmin {

	
    @Autowired
    private ProductService productService;
    
    @Autowired
    private OrderService orderService;
    
    @GetMapping("/addProduct")
    public String showAddProductPage(Model model) {
        model.addAttribute("products", new Products());
        return "AddPro"; 
    }
     

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("products") Products product, RedirectAttributes redirectAttributes) {
        productService.save(product);
        redirectAttributes.addFlashAttribute("message", "Product '" + product.getProductName() + "' added successfully!");
        return "redirect:/admin/home";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Product (ID: " + id + ") deleted successfully!");
        return "redirect:/admin/home";
    }
  
    @GetMapping("/home")
    public String getAllProducts(Model model) {
        List<Products> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "AdminHome";
    }
    
    @GetMapping("/search")
    public String findByProductNameLike(@RequestParam String pattern, Model model) {
        List<Products> products = productService.findByProductNameLike("%" + pattern + "%");
        model.addAttribute("searchResults", products);
        return "AdminHome";
    }
    

    @GetMapping("/edit/{id}")
    public String showEditProductPage(@PathVariable Long id, Model model) {
        Products product = productService.getProductById(id);
        if (product != null) {
            model.addAttribute("products", product);
            return "UpdatePro";
        } else {
            model.addAttribute("errorMessage", "Product with ID " + id + " not found.");
            return "errorPage";
        }
    }


    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute("products") Products updatedProduct, RedirectAttributes redirectAttributes) {
    	updatedProduct.setProductId(id);
        productService.update(id, updatedProduct);
        redirectAttributes.addFlashAttribute("message", "Product '" + updatedProduct.getProductName() + "' updated successfully!");
        return "redirect:/admin/home";
    }

    @GetMapping("/orders")
    public String getAllOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "AdminOrderHome";
    }

    @GetMapping("/orders/updateStatus/{orderId}")
    public String showUpdateOrderStatusForm(@PathVariable Long orderId, Model model) {
        Order order = orderService.getOrderDetails(orderId);
        if (order != null) {
            model.addAttribute("order", order);
            model.addAttribute("statuses", OrderStatus.values());
            return "orderUpdate";
        } else {
            model.addAttribute("errorMessage", "Order with ID " + orderId + " not found.");
            return "errorPage";
        }
    }

    @PostMapping("/orders/updateStatus")
    public String updateOrderStatus(@RequestParam Long orderId,
                                    @RequestParam OrderStatus newStatus,
                                    RedirectAttributes redirectAttributes) {
        orderService.updateOrderStatus(orderId, newStatus);
        redirectAttributes.addFlashAttribute("message", "Order (ID: " + orderId + ") status updated to " + newStatus.name() + " successfully!");
        return "redirect:/admin/orders";
    }

    @GetMapping("/orders/details/{orderId}")
    public String showOrderDetailsForAdmin(@PathVariable Long orderId, Model model) {
        Order order = orderService.getOrderDetails(orderId);
        if (order != null) {
            model.addAttribute("order", order);
            return "OrderDetailsAdmin";
        } else {
            model.addAttribute("errorMessage", "Order with ID " + orderId + " not found.");
            return "errorPage";
        }
    }
    
}

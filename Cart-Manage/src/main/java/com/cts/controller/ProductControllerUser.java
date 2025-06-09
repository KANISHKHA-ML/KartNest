package com.cts.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.cts.entity.*;
import com.cts.service.*;


@Controller

@RequestMapping("/user")
public class ProductControllerUser {

    @Autowired
    private ProductService service;

    @GetMapping("/home")
    public String getAllProducts(Model model) {
        List<Products> products = service.getAllProducts();
       model.addAttribute("products", products);
        return "displayProduct";
    }

    @GetMapping("/search")
    public String findByProductNameLike(@RequestParam String pattern, Model model) {
        List<Products> products = service.findByProductNameLike("%" +pattern + "%");
        model.addAttribute("products", products);
        return "displayProduct";
    }
}

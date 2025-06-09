package com.cts.service;
 
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;
 
import java.util.Arrays;

import java.util.Optional;

import java.util.List;
 
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;

import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
 
import com.cts.entity.Products;

import com.cts.repository.ProductRepo;
 
@ExtendWith(MockitoExtension.class)

public class ProductServiceImplTest {
 
    @Mock

    private ProductRepo repo;
 
    @InjectMocks

    private ProductServiceImpl productService;
 
    private Products product1, product2;
 
    @BeforeEach

    void setUp() {

        product1 = new Products();

        product1.setProductId(1L);

        product1.setProductName("Laptop");

        product1.setDescription("High-end gaming laptop");

        product1.setPrice(1500);

        product1.setNumberOfStock(10);
 
        product2 = new Products();

        product2.setProductId(2L);

        product2.setProductName("Smartphone");

        product2.setDescription("Latest flagship smartphone");

        product2.setPrice(999);

        product2.setNumberOfStock(20);

    }
 
    @Test

    void testGetAllProducts() {

        when(repo.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<Products> products = productService.getAllProducts();

        assertEquals(2, products.size());

        verify(repo, times(1)).findAll();

    }
 
    @Test

    void testFindByProductNameLike() {

        when(repo.findByProductNameLike("Lap")).thenReturn(Arrays.asList(product1));

        List<Products> products = productService.findByProductNameLike("Lap");

        assertEquals(1, products.size());

        assertEquals("Laptop", products.get(0).getProductName());

        verify(repo, times(1)).findByProductNameLike("Lap");

    }
 
    @Test

    void testSaveProduct() {

        productService.save(product1);

        verify(repo, times(1)).save(product1);

    }
 
    @Test

    void testDeleteProduct() {

        when(repo.existsById(1L)).thenReturn(true);

        boolean isDeleted = productService.delete(1L);

        assertTrue(isDeleted);

        verify(repo, times(1)).deleteById(1L);

    }
 
    @Test

    void testUpdateProduct() {

        when(repo.existsById(1L)).thenReturn(true);

        productService.update(1L, product2);

        assertEquals(1L, product2.getProductId());

        verify(repo, times(1)).save(product2);

    }
 
    @Test

    void testGetProductById() {

        when(repo.findById(1L)).thenReturn(Optional.of(product1));

        Products fetchedProduct = productService.getProductById(1L);

        assertNotNull(fetchedProduct);

        assertEquals(1L, fetchedProduct.getProductId());

        verify(repo, times(1)).findById(1L);

    }

}

 
package com.company.inventory.product_inventory.service;

import com.company.inventory.product_inventory.data.ProductData;
import com.company.inventory.product_inventory.data.WarehouseData;
import com.company.inventory.product_inventory.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService.deleteAllProducts();

        productService.createProduct(ProductData.getProductAmericanChocolate());
        productService.createProduct(ProductData.getProductBrazilianChocolate());
    }
    

    @Test
    void createProduct() {
        productService.createProduct(ProductData.getProductFranceChocolate());
        assertNotNull(productService.getProductBySku(3));
    }

    @Test
    void updateProduct() {
        assertEquals("Brazilian chocolate", productService.getProductBySku(1).getName());
        Product productBySku = productService.getProductBySku(1);
        productBySku.setName("France chocolate");
        productService.updateProduct(1, productBySku);
        assertEquals("France chocolate", productService.getProductBySku(1).getName());
    }

    @Test
    void addWarehouse() {
        assertEquals(2, productService.getProductBySku(1).getInventory().getWarehouses().size());
        productService.addWarehouse(1, WarehouseData.getWarehousesEcommerceMG());
        assertEquals(3, productService.getProductBySku(1).getInventory().getWarehouses().size());
    }

    @Test
    void updateWarehouseQuantity() {
        assertEquals(12, productService.getProductBySku(1).getInventory().getWarehouses().getFirst().getQuantity());
        productService.updateWarehouseQuantity(1,"SP", "ECOMMERCE", 3, "increment");
        assertEquals(15, productService.getProductBySku(1).getInventory().getWarehouses().getFirst().getQuantity());
        productService.updateWarehouseQuantity(1,"SP", "ECOMMERCE", 5, "decrement");
        assertEquals(10, productService.getProductBySku(1).getInventory().getWarehouses().getFirst().getQuantity());
    }

    @Test
    void removeWarehouse() {
        assertEquals(2, productService.getProductBySku(1).getInventory().getWarehouses().size());
        productService.removeWarehouse(1,"SP", "ECOMMERCE");
        assertEquals(1, productService.getProductBySku(1).getInventory().getWarehouses().size());
    }

    @Test
    void deleteAllProducts() {
        assertNotNull(productService.getAllProducts());
        productService.deleteAllProducts();
        assertEquals(new ArrayList<>(), productService.getAllProducts());
    }

    @Test
    void deleteProduct() {
        assertNotNull(productService.getProductBySku(1));
        productService.deleteProductBySku(1);
    }

    @Test
    void getProductBySku() {
        assertNotNull(productService.getProductBySku(1));
    }

    @Test
    void getAllProducts() {
        List<Product> products = productService.getAllProducts();
        assertEquals(2, products.size());
    }

    @Test
    void getProductsByLocality() {
        assertEquals(1, productService.getProductsByLocality("PE").size());
        assertEquals(2, productService.getProductsByLocality("SP").size());
        assertEquals(1, productService.getProductsByLocality("RJ").size());
    }

    @Test
    void getProductsByType() {
        assertEquals(2, productService.getProductsByType("ECOMMERCE").size());
        assertEquals(2, productService.getProductsByType("PHYSICAL_STORE").size());
    }
}
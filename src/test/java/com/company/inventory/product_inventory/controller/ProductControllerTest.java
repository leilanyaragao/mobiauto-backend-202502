package com.company.inventory.product_inventory.controller;

import com.company.inventory.product_inventory.data.ProductData;
import com.company.inventory.product_inventory.data.WarehouseData;
import com.company.inventory.product_inventory.dto.ProductResponseDTO;
import com.company.inventory.product_inventory.model.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class ProductControllerTest {

    @Autowired
    private ProductController productController;

    @BeforeEach
    void setUp() {
        productController.deleteAllProducts();

        productController.createProduct(ProductData.getProductAmericanChocolate());
        productController.createProduct(ProductData.getProductBrazilianChocolate());
    }


    @Test
    void createProduct() {
        productController.createProduct(ProductData.getProductFranceChocolate());
        assertNotNull(productController.getProductBySku(3));
    }

    @Test
    void updateProduct() {
        assertEquals("Brazilian chocolate", productController.getProductBySku(1).getName());
        Product productBySku = productController.getProductBySku(1);
        productBySku.setName("France chocolate");
        productController.updateProduct(1, productBySku);
        assertEquals("France chocolate", productController.getProductBySku(1).getName());
    }

    @Test
    void addWarehouseToProduct() {
        assertEquals(2, productController.getProductBySku(1).getInventory().getWarehouses().size());
        productController.addWarehouseToProduct(1,WarehouseData.getWarehousesEcommerceMG());
        assertEquals(3, productController.getProductBySku(1).getInventory().getWarehouses().size());
    }

    @Test
    void updateWarehouseQuantity() {
        assertEquals(12, productController.getProductBySku(1).getInventory().getWarehouses().getFirst().getQuantity());
        productController.updateWarehouseQuantity(1,"SP", "ECOMMERCE", 3, "increment");
        assertEquals(15, productController.getProductBySku(1).getInventory().getWarehouses().getFirst().getQuantity());
        productController.updateWarehouseQuantity(1,"SP", "ECOMMERCE", 5, "decrement");
        assertEquals(10, productController.getProductBySku(1).getInventory().getWarehouses().getFirst().getQuantity());
    }


    @Test
    void removeWarehouseFromProduct() {
        assertEquals(2, productController.getProductBySku(1).getInventory().getWarehouses().size());
        productController.removeWarehouseFromProduct(1,"SP", "ECOMMERCE");
        assertEquals(1, productController.getProductBySku(1).getInventory().getWarehouses().size());
    }

    @Test
    void deleteAllProducts() {
        assertNotNull(productController.getAllProducts());
        productController.deleteAllProducts();
        assertEquals(new ArrayList<>(), productController.getAllProducts());
    }

    @Test
    void deleteProduct() {
        assertNotNull(productController.getProductBySku(1));
        productController.deleteProduct(1);
    }

    @Test
    void getProductBySku() {
        assertNotNull(productController.getProductBySku(1));
    }

    @Test
    void getAllProducts() {
        List<Product> products = productController.getAllProducts();
        assertEquals(2, products.size());
    }

    @Test
    void getProductsByLocality() {
        assertEquals(1, productController.getProductsByLocality("PE").size());
        assertEquals(2, productController.getProductsByLocality("SP").size());
        assertEquals(1, productController.getProductsByLocality("RJ").size());
    }

    @Test
    void getProductsByType() {
        assertEquals(2, productController.getProductsByType("ECOMMERCE").size());
        assertEquals(2, productController.getProductsByType("PHYSICAL_STORE").size());
    }
}
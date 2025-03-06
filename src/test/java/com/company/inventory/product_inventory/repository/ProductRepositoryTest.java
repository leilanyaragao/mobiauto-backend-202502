package com.company.inventory.product_inventory.repository;

import com.company.inventory.product_inventory.data.ProductData;
import com.company.inventory.product_inventory.model.Product;
import com.company.inventory.product_inventory.model.WarehouseType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        productRepository.saveAll(ProductData.getProjects());
    }

    @Test
    void findProductBySku() {
        Product productBySku = productRepository.findProductBySku(1);
        assertNotNull(productRepository.findProductBySku(1));
        assertEquals(1, productBySku.getSku());
        assertEquals("Brazilian chocolate", productBySku.getName());

    }

    @Test
    void findByWarehouseLocality() {
        List<Product> productsByWarehouseLocality = productRepository.findByWarehouseLocality("SP");
        assertNotNull(productsByWarehouseLocality);
        assertEquals(3, productsByWarehouseLocality.size());
        assertEquals("Brazilian chocolate", productsByWarehouseLocality.get(0).getName());
        assertEquals("American chocolate", productsByWarehouseLocality.get(1).getName());
        assertEquals("France chocolate", productsByWarehouseLocality.get(2).getName());

    }

    @Test
    void findByWarehouseType() {
        List<Product> productsByWarehouseType = productRepository.findByWarehouseType(WarehouseType.PHYSICAL_STORE);
        assertNotNull(productsByWarehouseType);
        assertEquals(2, productsByWarehouseType.size());
        assertEquals("Brazilian chocolate", productsByWarehouseType.get(0).getName());
        assertEquals("American chocolate", productsByWarehouseType.get(1).getName());
    }
}
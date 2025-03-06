package com.company.inventory.product_inventory.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductTest {

    @Test
    void createProductTest() {
        List<Warehouse> warehouses = new ArrayList<>();
        warehouses.add(new Warehouse("SP", 12, WarehouseType.ECOMMERCE));
        warehouses.add(new Warehouse("MOEMA", 3, WarehouseType.PHYSICAL_STORE));

        Inventory inventory = new Inventory(15, warehouses);

        Product product = new Product(43264, "Brazilian chocolate", inventory);

        assertEquals(43264, product.getSku());
        assertEquals("Brazilian chocolate", product.getName());
        assertEquals(15, product.getInventory().getQuantity());
        assertTrue(product.isMarketable());
    }
}
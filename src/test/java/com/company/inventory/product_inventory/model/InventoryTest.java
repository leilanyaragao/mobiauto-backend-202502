package com.company.inventory.product_inventory.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    @Test
    void createInventoryTest() {
        List<Warehouse> warehouses = new ArrayList<>();
        warehouses.add(new Warehouse("SP", 12, "ECOMMERCE"));
        warehouses.add(new Warehouse("MOEMA", 3, "PHYSICAL_STORE"));

        Inventory inventory = new Inventory(15, warehouses);

        assertEquals(15, inventory.getQuantity());
        assertEquals(warehouses, inventory.getWarehouses());
    }
}
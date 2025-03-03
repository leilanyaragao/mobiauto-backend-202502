package com.company.inventory.product_inventory.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WarehouseTest {

    @Test
    void createWarehouseTest() {
        Warehouse warehouse = new Warehouse("MOEMA", 3, "PHYSICAL_STORE");

        assertEquals("MOEMA", warehouse.getLocality());
        assertEquals(3, warehouse.getQuantity());
        assertEquals("PHYSICAL_STORE", warehouse.getType());
    }
}
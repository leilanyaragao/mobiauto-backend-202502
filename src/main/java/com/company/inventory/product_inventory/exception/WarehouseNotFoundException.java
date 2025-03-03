package com.company.inventory.product_inventory.exception;

public class WarehouseNotFoundException extends IllegalArgumentException {
    public WarehouseNotFoundException(String message) {
        super(message);
    }
}
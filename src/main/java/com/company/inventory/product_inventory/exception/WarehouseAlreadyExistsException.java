package com.company.inventory.product_inventory.exception;

public class WarehouseAlreadyExistsException extends RuntimeException{
     public WarehouseAlreadyExistsException(String message) {
        super(message);
    }
}

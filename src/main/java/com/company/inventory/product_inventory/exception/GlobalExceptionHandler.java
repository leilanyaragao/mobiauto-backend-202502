package com.company.inventory.product_inventory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientQuantityException.class)
    public ResponseEntity<String> handleInsufficientQuantity(InsufficientQuantityException message) {
        return new ResponseEntity<>(message.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<String> handleInvalidOperation(InvalidOperationException message) {
        return new ResponseEntity<>(message.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<String> handleProductAlreadyExists(ProductAlreadyExistsException message) {
        return new ResponseEntity<>(message.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFound(ProductNotFoundException message) {
        return new ResponseEntity<>(message.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WarehouseAlreadyExistsException.class)
    public ResponseEntity<String> handleWarehouseAlreadyExists(WarehouseAlreadyExistsException message) {
        return new ResponseEntity<>(message.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(WarehouseNotFoundException.class)
    public ResponseEntity<String> handleWarehouseNotFound(WarehouseNotFoundException message) {
        return new ResponseEntity<>(message.getMessage(), HttpStatus.NOT_FOUND);
    }

}

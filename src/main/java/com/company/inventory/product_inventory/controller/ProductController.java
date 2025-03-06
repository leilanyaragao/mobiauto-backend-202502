package com.company.inventory.product_inventory.controller;

import com.company.inventory.product_inventory.dto.ProductResponseDTO;
import com.company.inventory.product_inventory.model.Product;
import com.company.inventory.product_inventory.model.QuantityOperation;
import com.company.inventory.product_inventory.model.Warehouse;
import com.company.inventory.product_inventory.model.WarehouseType;
import com.company.inventory.product_inventory.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/products")
@Validated
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody Product product) {
        productService.createProduct(product);
    }

    @PutMapping("/sku/{sku}")
    @ResponseStatus(HttpStatus.OK)
    public void updateProduct(@PathVariable Integer sku, @RequestBody Product product) {
        productService.updateProduct(sku, product);
    }

    @PatchMapping("/sku/{sku}/add-warehouse")
    @ResponseStatus(HttpStatus.OK)
    public void addWarehouseToProduct(
            @PathVariable Integer sku,
            @RequestBody Warehouse warehouse) {
        productService.addWarehouse(sku, warehouse);
    }

    @PatchMapping("/sku/{sku}/update-warehouse-quantity")
    @ResponseStatus(HttpStatus.OK)
    public void updateWarehouseQuantity(
            @PathVariable Integer sku,
            @RequestParam String locality,
            @RequestParam WarehouseType type,
            @RequestParam int quantityChange,
            @RequestParam QuantityOperation operation) {
        productService.updateWarehouseQuantity(sku, locality, type, quantityChange, operation);
    }

    @DeleteMapping("/sku/{sku}/delete-warehouse")
    @ResponseStatus(HttpStatus.OK)
    public void removeWarehouseFromProduct(
            @PathVariable Integer sku,
            @RequestParam String locality,
            @RequestParam WarehouseType type) {
        productService.removeWarehouse(sku, locality, type);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteAllProducts() {
        productService.deleteAllProducts();
    }

    @DeleteMapping("/sku/{sku}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteProduct(@PathVariable Integer sku) {
        productService.deleteProductBySku(sku);
    }

    @GetMapping("/sku/{sku}")
    @ResponseStatus(HttpStatus.OK)
    public Product getProductBySku(@PathVariable Integer sku) {
        return productService.getProductBySku(sku);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/warehouse-by-locality")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponseDTO> getProductsByLocality(@RequestParam String locality) {
        return productService.getProductsByLocality(locality);
    }

    @GetMapping("/warehouse-by-type")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponseDTO> getProductsByType(@RequestParam WarehouseType type) {
        return productService.getProductsByType(type);
    }
}

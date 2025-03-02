package com.company.inventory.product_inventory.controller;

import com.company.inventory.product_inventory.dto.ProductResponseDTO;
import com.company.inventory.product_inventory.model.Product;
import com.company.inventory.product_inventory.model.Warehouse;
import com.company.inventory.product_inventory.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @PutMapping("/sku/{sku}")
    @ResponseStatus(HttpStatus.OK)
    public Product updateProduct(@PathVariable Integer sku, @RequestBody Product product) {
        return productService.updateProduct(sku, product);
    }

    @PatchMapping("/sku/{sku}/add-warehouse")
    @ResponseStatus(HttpStatus.OK)
    public Product addWarehouseToProduct(
            @PathVariable Integer sku,
            @RequestBody Warehouse warehouse) {
        return productService.addWarehouse(sku, warehouse);
    }

    @PatchMapping("/sku/{sku}/update-warehouse-quantity")
    @ResponseStatus(HttpStatus.OK)
    public Product updateWarehouseQuantity(
            @PathVariable Integer sku,
            @RequestParam String locality,
            @RequestParam String type,
            @RequestParam int quantityChange,
            @RequestParam String operation) {
        return productService.updateWarehouseQuantity(sku, locality, type, quantityChange, operation);
    }

    @DeleteMapping("/sku/{sku}/delete-warehouse")
    public ResponseEntity<Product> removeWarehouseFromProduct(
            @PathVariable Integer sku,
            @RequestParam String locality,
            @RequestParam String type) {
        Product updatedProduct = productService.removeWarehouse(sku, locality, type);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllProducts() {
        productService.deleteAllProducts();
    }

    @DeleteMapping("/sku/{sku}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Integer sku) {
        productService.deleteProduct(sku);
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
    public List<ProductResponseDTO> getProductsByLocality(@RequestParam String locality) {
        return productService.getProductsByLocality(locality);
    }

    @GetMapping("/warehouse-by-type")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByType(@RequestParam String type) {
        List<ProductResponseDTO> products = productService.getProductsByType(type);
        return ResponseEntity.ok(products);
    }
}

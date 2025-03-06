package com.company.inventory.product_inventory.service;

import com.company.inventory.product_inventory.dto.LocationQuantityResponseDTO;
import com.company.inventory.product_inventory.dto.ProductResponseDTO;
import com.company.inventory.product_inventory.dto.TypeQuantityResponseDTO;
import com.company.inventory.product_inventory.exception.*;
import com.company.inventory.product_inventory.model.Product;
import com.company.inventory.product_inventory.model.Warehouse;
import com.company.inventory.product_inventory.model.WarehouseType;
import com.company.inventory.product_inventory.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public void createProduct(Product product) {
        if (productRepository.existsById(product.getSku())) {
            throw new ProductAlreadyExistsException("Product with sku " + product.getSku() + " already exists.");
        }
        product.getInventory().quantity();
        product.updateMarketableStatus();
        productRepository.save(product);
    }

    public void updateProduct(Integer sku, Product product) {
        validateProductExistence(sku);
        validateSkuConsistency(sku, product);

        Product productBySku = productRepository.findProductBySku(sku);
        updateProductFields(product, productBySku);

        productRepository.save(productBySku);
    }

    public void addWarehouse(Integer sku, Warehouse warehouse) {
        validateProductExistence(sku);
        Product product = getProductBySku(sku);
        Optional<Warehouse> existingWarehouse = findWarehouse(product, warehouse);

        if (existingWarehouse.isPresent()) {
            throw new WarehouseAlreadyExistsException("This warehouse already exist for this product " + product.getSku() + ". You can update warehouse quantity.");
        } else {
            product.addWarehouse(warehouse);
        }

        productRepository.save(product);
    }

    public void updateWarehouseQuantity(Integer sku, String locality, WarehouseType type, int quantityChange, String operation) {
        validateProductExistence(sku);
        Product product = getProductBySku(sku);
        Warehouse warehouse = findWarehouseByLocalityAndType(product, locality, type)
                .orElseThrow(() -> new WarehouseNotFoundException("Warehouse not found for locality: " + locality + " and type: " + type));

        adjustWarehouseQuantity(warehouse, quantityChange, operation);
        updateTotalQuantity(product);

        productRepository.save(product);
    }

    public void removeWarehouse(Integer sku, String locality, WarehouseType type) {
        validateProductExistence(sku);
        Product product = getProductBySku(sku);
        Warehouse warehouse = findWarehouseByLocalityAndType(product, locality, type)
                .orElseThrow(() -> new WarehouseNotFoundException("Warehouse not found for locality: " + locality + " and type: " + type));

        product.getInventory().getWarehouses().remove(warehouse);
        updateTotalQuantity(product);

        productRepository.save(product);
    }

    public void deleteAllProducts() {
        productRepository.deleteAll();
    }

    public void deleteProductBySku(Integer sku) {
        validateProductExistence(sku);
        productRepository.deleteById(sku);
    }

    public Product getProductBySku(Integer sku) {
        return productRepository.findById(sku)
                .orElseThrow(() -> new NotFoundException("Product with sku " + sku + " not found."));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<ProductResponseDTO> getProductsByLocality(String locality) {
        List<Product> products = productRepository.findByWarehouseLocality(locality);
        if (products.isEmpty()) {
            throw new ProductNotFoundException("No products found for the location: " + locality);
        }

        List<ProductResponseDTO> productResponseDTOS = new ArrayList<>();

        for (Product product : products) {
            List<TypeQuantityResponseDTO> typeQuantityResponseDTO = new ArrayList<>();
            for (Warehouse warehouse : product.getInventory().getWarehouses()) {
                if (warehouse.getLocality().equals(locality)) {
                    typeQuantityResponseDTO.add(new TypeQuantityResponseDTO(warehouse.getType(), warehouse.getQuantity()));
                }
            }
            productResponseDTOS.add(new ProductResponseDTO(product.getSku(), product.getName(), null, typeQuantityResponseDTO));
        }

        return productResponseDTOS;
    }

    public List<ProductResponseDTO> getProductsByType(WarehouseType type) {
        List<Product> products = productRepository.findByWarehouseType(type);
        if (products.isEmpty()) {
            throw new ProductNotFoundException("No products found for type: " + type);
        }

        List<ProductResponseDTO> productResponseDTOS = new ArrayList<>();
        for (Product product : products) {
            List<LocationQuantityResponseDTO> locationQuantityResponseDTO = new ArrayList<>();
            for (Warehouse warehouse : product.getInventory().getWarehouses()) {
                if (warehouse.getType().equals(type)) {
                    locationQuantityResponseDTO.add(new LocationQuantityResponseDTO(warehouse.getLocality(), warehouse.getQuantity()));
                }
            }
            productResponseDTOS.add(new ProductResponseDTO(product.getSku(), product.getName(), locationQuantityResponseDTO, null));
        }
        return productResponseDTOS;
    }

    private void validateProductExistence(Integer sku) {
        if (!productRepository.existsById(sku)) {
            throw new ProductNotFoundException("Product with sku " + sku + " not found.");
        }
    }

    private void validateSkuConsistency(Integer sku, Product product) {
        if (!sku.equals(product.getSku())) {
            throw new IllegalArgumentException("SKU cannot be changed.");
        }
    }

    private void updateProductFields(Product source, Product target) {
        target.setSku(source.getSku());
        target.setName(source.getName());
        target.setInventory(source.getInventory());
        target.setMarketable(source.isMarketable());
        target.getInventory().quantity();
        target.updateMarketableStatus();
    }

    private Optional<Warehouse> findWarehouse(Product product, Warehouse warehouse) {
        return product.getInventory().getWarehouses().stream()
                .filter(w -> w.getType().equals(warehouse.getType()) && w.getLocality().equals(warehouse.getLocality()))
                .findFirst();
    }

    private Optional<Warehouse> findWarehouseByLocalityAndType(Product product, String locality, WarehouseType type) {
        return product.getInventory().getWarehouses().stream()
                .filter(w -> w.getLocality().equals(locality) && w.getType().equals(type))
                .findFirst();
    }

    private void adjustWarehouseQuantity(Warehouse warehouse, int quantityChange, String operation) {
        int currentQuantity = warehouse.getQuantity();
        if ("decrement".equalsIgnoreCase(operation)) {
            if (quantityChange > currentQuantity) {
                throw new InsufficientQuantityException("Cannot subtract more than the available quantity.");
            }
            warehouse.setQuantity(currentQuantity - quantityChange);
        } else if ("increment".equalsIgnoreCase(operation)) {
            warehouse.setQuantity(currentQuantity + quantityChange);
        } else {
            throw new IllegalArgumentException("Invalid operation type. Use 'increment' or 'decrement'.");
        }
    }

    private void updateTotalQuantity(Product product) {
        int totalQuantity = product.getInventory().getWarehouses().stream()
                .mapToInt(Warehouse::getQuantity)
                .sum();
        product.getInventory().setQuantity(totalQuantity);
        product.updateMarketableStatus();
    }
}

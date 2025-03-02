package com.company.inventory.product_inventory.service;

import com.company.inventory.product_inventory.dto.LocationQuantityResponseDTO;
import com.company.inventory.product_inventory.dto.ProductResponseDTO;
import com.company.inventory.product_inventory.dto.TypeQuantityResponseDTO;
import com.company.inventory.product_inventory.exception.*;
import com.company.inventory.product_inventory.model.Product;
import com.company.inventory.product_inventory.model.Warehouse;
import com.company.inventory.product_inventory.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public Product createProduct(Product product) {
        if (productRepository.existsById(product.getSku())) {
            throw new ProductAlreadyExistsException("Product with sku " + product.getSku() + " already exists.");
        }
        product.getInventory().quantity();
        product.updateMarketableStatus();
        return productRepository.save(product);
    }

    public Product updateProduct(Integer sku, Product product) {
        validateProductExistence(sku);
        validateSkuConsistency(sku, product);

        Product productBySku = productRepository.findProductBySku(sku);
        updateProductFields(product, productBySku);

        return productRepository.save(productBySku);
    }
    public Product getProductBySku(Integer sku) {
        return productRepository.findById(sku)
                .orElseThrow(() -> new ProductNotFoundException("Product with sku " + sku + " not found."));
    }

    public void deleteProduct(Integer sku) {
        validateProductExistence(sku);
        productRepository.deleteById(sku);
    }

    public void deleteAllProducts() {
        productRepository.deleteAll();
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


    public Product addWarehouse(Integer sku, Warehouse warehouse) {
        Product product = getProductBySku(sku);
        Optional<Warehouse> existingWarehouse = findWarehouse(product, warehouse);

        if (existingWarehouse.isPresent()) {
            existingWarehouse.get().setQuantity(existingWarehouse.get().getQuantity() + warehouse.getQuantity());
        } else {
            product.addWarehouse(warehouse);
        }

        return productRepository.save(product);
    }

    public Product removeWarehouse(Integer sku, String locality, String type) {
        Product product = getProductBySku(sku);
        Warehouse warehouse = findWarehouseByLocalityAndType(product, locality, type)
                .orElseThrow(() -> new WarehouseNotFoundException("Warehouse not found for locality: " + locality + " and type: " + type));

        product.getInventory().getWarehouses().remove(warehouse);
        updateTotalQuantity(product);

        return productRepository.save(product);
    }

    public Product updateWarehouseQuantity(Integer sku, String locality, String type, int quantityChange, String operation) {
        Product product = getProductBySku(sku);
        Warehouse warehouse = findWarehouseByLocalityAndType(product, locality, type)
                .orElseThrow(() -> new WarehouseNotFoundException("Warehouse not found for locality: " + locality + " and type: " + type));

        adjustWarehouseQuantity(warehouse, quantityChange, operation);
        updateTotalQuantity(product);

        return productRepository.save(product);
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

    private Optional<Warehouse> findWarehouseByLocalityAndType(Product product, String locality, String type) {
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
            throw new InvalidOperationException("Invalid operation type. Use 'increment' or 'decrement'.");
        }
    }

    private void updateTotalQuantity(Product product) {
        int totalQuantity = product.getInventory().getWarehouses().stream()
                .mapToInt(Warehouse::getQuantity)
                .sum();
        product.getInventory().setQuantity(totalQuantity);
        product.updateMarketableStatus();
    }


    public List<ProductResponseDTO> getProductsByLocality(String locality) {
        List<Product> products = productRepository.findByWarehouseLocality(locality);
        if (products.isEmpty()) {
            throw new ProductNotFoundException("Nenhum produto encontrado para a localidade: " + locality);
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

    public List<ProductResponseDTO> getProductsByType(String type) {
        List<Product> products = productRepository.findByWarehouseType(type);
        if (products.isEmpty()) {
            throw new ProductNotFoundException("Nenhum produto encontrado para o tipo: " + type);
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


}

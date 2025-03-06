package com.company.inventory.product_inventory.repository;

import com.company.inventory.product_inventory.model.Product;
import com.company.inventory.product_inventory.model.WarehouseType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, Integer> {
    Product findProductBySku(Integer sku);

    @Query("{'inventory.warehouses.locality': ?0}")
    List<Product> findByWarehouseLocality(String locality);

    @Query("{'inventory.warehouses.type': ?0}")
    List<Product> findByWarehouseType(WarehouseType type);
}

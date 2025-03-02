package com.company.inventory.product_inventory.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    private Integer sku;
    private String name;
    private Inventory inventory;
    private boolean isMarketable;

    public Product(int sku, String name, Inventory inventory) {
        this.sku = sku;
        this.name = name;
        this.inventory = inventory;
        updateMarketableStatus();
    }

    public void addWarehouse(Warehouse warehouse) {
        inventory.addWarehouse(warehouse);
        updateMarketableStatus();
    }

    public void updateMarketableStatus() {
        this.isMarketable = inventory.calculateQuantity() > 0;
    }
}

package com.company.inventory.product_inventory.data;


import com.company.inventory.product_inventory.model.Inventory;

public class InventoryData {

    public static Inventory getInventoryAmericanChocolate() {
        return new Inventory(
                20,
                WarehouseData.getWarehousesAmericanChocolate()
        );
    }

    public static Inventory getInventoryBrazilianChocolate() {
        return new Inventory(
                15,
                WarehouseData.getWarehousesBrazilianChocolate()
        );
    }

    public static Inventory getInventoryFranceChocolate() {
        return new Inventory(
                1,
                WarehouseData.getWarehousesFranceChocolate()
        );
    }

}

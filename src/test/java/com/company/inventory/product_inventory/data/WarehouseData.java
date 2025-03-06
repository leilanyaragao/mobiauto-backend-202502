package com.company.inventory.product_inventory.data;


import com.company.inventory.product_inventory.model.Warehouse;
import com.company.inventory.product_inventory.model.WarehouseType;

import java.util.ArrayList;
import java.util.List;

public class WarehouseData {

    public static List<Warehouse> getWarehousesAmericanChocolate(){
        List<Warehouse> warehouses = new ArrayList<>();
        warehouses.add(new Warehouse("SP", 12, WarehouseType.ECOMMERCE));
        warehouses.add(new Warehouse("RJ", 3, WarehouseType.PHYSICAL_STORE));
        warehouses.add(new Warehouse("PE", 5, WarehouseType.PHYSICAL_STORE));
        return warehouses;
    }

    public static List<Warehouse> getWarehousesBrazilianChocolate(){
        List<Warehouse> warehouses = new ArrayList<>();
        warehouses.add(new Warehouse("SP", 12, WarehouseType.ECOMMERCE));
        warehouses.add(new Warehouse("SP", 3, WarehouseType.PHYSICAL_STORE));
        return warehouses;
    }

    public static List<Warehouse> getWarehousesFranceChocolate(){
        List<Warehouse> warehouses = new ArrayList<>();
        warehouses.add(new Warehouse("SP", 1, WarehouseType.ECOMMERCE));
        return warehouses;
    }

    public static Warehouse getWarehousesEcommerceMG(){
        return new Warehouse("MG", 1, WarehouseType.ECOMMERCE);
    }

    public static Warehouse getWarehousesEcommerceSPBrazilianChocolate(){
        return new Warehouse("SP", 2, WarehouseType.ECOMMERCE);
    }
}

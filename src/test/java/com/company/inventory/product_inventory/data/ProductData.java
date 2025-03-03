package com.company.inventory.product_inventory.data;


import com.company.inventory.product_inventory.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductData {

    public static Product getProductBrazilianChocolate() {
        return new Product(
                1,
                "Brazilian chocolate",
                InventoryData.getInventoryBrazilianChocolate()
        );
    }

    public static Product getProductAmericanChocolate() {
        return new Product(
                2,
                "American chocolate",
                InventoryData.getInventoryAmericanChocolate()
        );
    }

    public static Product getProductFranceChocolate() {
        return new Product(
                3,
                "France chocolate",
                InventoryData.getInventoryFranceChocolate()
        );
    }

    public static List<Product> getProjects() {
        List<Product> products = new ArrayList<>(List.of());
        products.add(
                new Product(
                        1,
                        "Brazilian chocolate",
                        InventoryData.getInventoryBrazilianChocolate()
                ));

        products.add(
                new Product(
                        2,
                        "American chocolate",
                        InventoryData.getInventoryAmericanChocolate()
                ));
        products.add(
                new Product(
                        3,
                        "France chocolate",
                        InventoryData.getInventoryFranceChocolate()
                ));
        return products;
    }


}

package com.company.inventory.product_inventory.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Warehouse {
    private String locality;
    private Integer quantity;
    private String type;
}

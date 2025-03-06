package com.company.inventory.product_inventory.dto;

import com.company.inventory.product_inventory.model.WarehouseType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TypeQuantityResponseDTO {
    private WarehouseType type;
    private Integer quantity;

    public TypeQuantityResponseDTO(WarehouseType type, Integer quantity) {
        this.type = type;
        this.quantity = quantity;
    }

}

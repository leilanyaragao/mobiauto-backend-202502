package com.company.inventory.product_inventory.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TypeQuantityResponseDTO {
    private String type;
    private Integer quantity;

    public TypeQuantityResponseDTO(String type, Integer quantity) {
        this.type = type;
        this.quantity = quantity;
    }

}

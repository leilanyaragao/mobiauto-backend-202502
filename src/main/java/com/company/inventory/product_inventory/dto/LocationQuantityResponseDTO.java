package com.company.inventory.product_inventory.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LocationQuantityResponseDTO {
    private String locality;
    private Integer quantity;

    public LocationQuantityResponseDTO(String locality, Integer quantity) {
        this.locality = locality;
        this.quantity = quantity;
    }

}
package com.company.inventory.product_inventory.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductResponseDTO {
    private Integer sku;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<LocationQuantityResponseDTO> warehouses;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TypeQuantityResponseDTO> type;

    public ProductResponseDTO(Integer sku, String name, List<LocationQuantityResponseDTO> warehouses, List<TypeQuantityResponseDTO> type) {
        this.sku = sku;
        this.name = name;
        this.warehouses = warehouses;
        this.type = type;
    }

    public ProductResponseDTO(Integer sku, String name) {
        this.sku = sku;
        this.name = name;
    }

}

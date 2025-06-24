package com.InventoryOrder.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDTO {
    private Long InventoryId;
    private String name;
    private Integer quantity;
    private Double price;
    private Double subtotal;
}
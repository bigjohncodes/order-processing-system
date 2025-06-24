package com.InventoryOrder.Events;

import com.InventoryOrder.DTOs.InventoryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCreationEvent {
    private Long OrderId;
    private List<InventoryDTO> p;
    private Double TotalAmount;
    private Double Quantity;
    private Date CreatedAt;

}

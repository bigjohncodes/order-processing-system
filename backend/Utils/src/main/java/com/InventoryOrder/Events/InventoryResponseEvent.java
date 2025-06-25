package com.InventoryOrder.Events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponseEvent {
    private Long orderId;
    private Long id;
    private String status; // "AVAILABLE" or "FAILURE"
    private String failureReason;
    private double price;
    private String productName;
}
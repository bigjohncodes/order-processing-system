package com.InventoryOrder.model;

import com.InventoryOrder.DTOs.InventoryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Id
    private String orderId;  // MongoDB uses String as the default type for ObjectId
    private Long InventoryId;
    private String status;
    private InventoryDTO Inventory; // Single Inventory
    private Double totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String transactionId;
    private String orderName;
}

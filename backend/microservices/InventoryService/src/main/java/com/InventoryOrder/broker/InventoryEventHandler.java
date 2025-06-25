package com.InventoryOrder.broker;

import com.InventoryOrder.Events.InventoryResponseEvent;
import com.InventoryOrder.Events.OrderEvent;
import com.InventoryOrder.Inventory;
import com.InventoryOrder.Repository.InventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InventoryEventHandler {

    @Autowired
    private InventoryRepository InventoryRepository;

    @Autowired
    private KafkaTemplate<String, InventoryResponseEvent> kafkaTemplate;

    @KafkaListener(topics = "order-event-topic", groupId = "InventoryGroup")
    public void consumeOrderEvent(OrderEvent orderEvent) {
        log.info("Order event received in Inventory service: {}", orderEvent.getOrderId());

        Long InventoryId = orderEvent.getInventoryId();
        int quantity = orderEvent.getQuantity();

        InventoryResponseEvent responseEvent = new InventoryResponseEvent();
        responseEvent.setOrderId(orderEvent.getOrderId());
        Inventory Inventory = InventoryRepository.findById(InventoryId).orElse(null);

        if (Inventory == null) {
            responseEvent.setStatus("FAILURE");
            responseEvent.setFailureReason("Inventory not found");
        } else if (Inventory.getStock() < quantity) {
            responseEvent.setStatus("FAILURE");
            responseEvent.setFailureReason("Insufficient stock");
        } else {
            Inventory.setStock(Inventory.getStock() - quantity);
            InventoryRepository.save(Inventory);

            responseEvent.setStatus("AVAILABLE");
            responseEvent.setId(InventoryId);
            responseEvent.setPrice(Inventory.getPrice() * quantity);
            responseEvent.setProductName(Inventory.getName());
        }
        kafkaTemplate.send("Inventory-Response-topic", responseEvent);
        log.info("Inventory response sent for order: {}", orderEvent.getOrderId());
    }
}
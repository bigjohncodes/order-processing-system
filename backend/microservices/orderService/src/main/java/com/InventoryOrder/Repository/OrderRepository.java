package com.InventoryOrder.Repository;

import com.InventoryOrder.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
    
}


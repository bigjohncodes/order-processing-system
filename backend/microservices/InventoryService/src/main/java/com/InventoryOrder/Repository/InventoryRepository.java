package com.InventoryOrder.Repository;

import com.InventoryOrder.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    boolean existsByNameIgnoreCase(String name);
}
package com.InventoryOrder.Repository;

import java.util.Optional;
import com.InventoryOrder.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    boolean existsByNameIgnoreCase(String name);
    Optional<Inventory> findByNameIgnoreCase(String name);
}
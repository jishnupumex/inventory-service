package com.supply.inventoryservice.repo;

import com.supply.inventoryservice.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepo extends JpaRepository<Inventory, Long> {
}
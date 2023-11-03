package com.supply.inventoryservice.repo;

import com.supply.inventoryservice.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface InventoryRepo extends JpaRepository<Inventory, Long> {
        @Query("SELECT i.prodQty FROM Inventory i WHERE i.prodId = :prodId")
        Integer getAvailableQty(@Param("prodId") Long prodId);
}

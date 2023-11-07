package com.supply.inventoryservice.service;

import com.scm.UserOrder;
import com.supply.inventoryservice.entity.Inventory;
import com.supply.inventoryservice.repo.InventoryRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.kafka.core.KafkaTemplate;

@Service
public class InventoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryService.class);
    private final InventoryRepo inventoryRepo;
    private final OrderFulfillmentService inventoryStockStatusService;
    private final SupplierStockAddingService supplierStockAddingService;
    private final KafkaTemplate<String, Inventory> kafkaTemplate;
//    private static final String KAFKA_TOPIC = "Inventory";

    @Autowired
    public InventoryService(InventoryRepo inventoryRepo, OrderFulfillmentService inventoryStockStatusService, SupplierStockAddingService supplierStockAddingService, KafkaTemplate<String, Inventory> kafkaTemplate) {
        this.inventoryRepo = inventoryRepo;
        this.inventoryStockStatusService = inventoryStockStatusService;
        this.supplierStockAddingService = supplierStockAddingService;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Inventory saveInventory(Inventory inventory) {
        Inventory savedInventory = inventoryRepo.save(inventory);
//        kafkaTemplate.send(KAFKA_TOPIC, savedInventory);
        return savedInventory;
    }

    public Inventory getInventoryByProdId(Long prodId) {
        Optional<Inventory> inventoryOptional = inventoryRepo.findById(prodId);

        if (inventoryOptional.isPresent()) {
            return inventoryOptional.get();
        }

        // Handle the case where the inventory entry doesn't exist.
        throw new EntityNotFoundException("Inventory entry not found for prodId: " + prodId);
    }
    @Transactional
    public void updateInventory(Long prodId, int prodQtyToAdd) {
        // Retrieve the existing inventory entry by prodId
        Inventory existingInventory = inventoryRepo.findById(prodId).orElse(null);
        if (existingInventory != null) {
            // Update the prodQty with the new value
            existingInventory.setProdQty(existingInventory.getProdQty() + prodQtyToAdd);
            inventoryRepo.save(existingInventory);
        } else {
            // Handle the case where the inventory entry doesn't exist.
            throw new EntityNotFoundException("Inventory entry not found for prodId: " + prodId);
        }
    }


    public List<Inventory> saveInventoryList(List<Inventory> inventoryList) {
        List<Inventory> savedInventoryList = new ArrayList<>();
        for (Inventory inventory : inventoryList) {
            Inventory savedInventory = inventoryRepo.save(inventory);
            savedInventoryList.add(savedInventory);

            // Publish the saved inventory entry to Kafka
//            kafkaTemplate.send(KAFKA_TOPIC, savedInventory);
        }
        return savedInventoryList;
    }

    public Optional<Inventory> findInventoryById(Long id) {
        return inventoryRepo.findById(id);
    }

    public List<Inventory> findAllInventory() {
        return inventoryRepo.findAll();
    }


    @KafkaListener(topics = "${spring.kafka.topic.name.topic1}", groupId = "${spring.kafka.consumer.group-id.topic1}")
    public void consumeUserOrderKafkaTopic(UserOrder order) {
        LOGGER.info("User Order received -> {}", order.toString());

        // Retrieve availableQty from the Inventory service
        int availableQty = inventoryRepo.getAvailableQty((long) order.getProdId());

        // Trigger the InventoryStockStatusService
        String availability = inventoryStockStatusService.checkProductAvailability(order);

        if (order.getProdQty() > availableQty) {
            // Send a request to the Supplier if prodQty is low based on availableQty
            supplierStockAddingService.sendRequestToSupplier(order, availableQty);
        }
        else{
            // Send the product availability message
            inventoryStockStatusService.sendProductToLogistics(order, availability);
        }
    }
}

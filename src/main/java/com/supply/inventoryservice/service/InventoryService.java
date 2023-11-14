package com.supply.inventoryservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supply.inventoryservice.entity.Inventory;
import com.supply.inventoryservice.entity.UserCart;
import com.supply.inventoryservice.entity.UserOrders;
import com.supply.inventoryservice.repo.InventoryRepo;
import com.supply.inventoryservice.repo.UserCartRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.kafka.core.KafkaTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepo inventoryRepo;
    private final UserCartRepository userCartRepository;
    private final OrderFulfillmentService inventoryStockStatusService;
    private final SupplierStockAddingService supplierStockAddingService;
    private final KafkaTemplate<String, Inventory> kafkaTemplate;
//    private static final String KAFKA_TOPIC = "Inventory";

    private final ObjectMapper objectMapper;

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
            existingInventory.setProdQty(existingInventory.getProdQty() - prodQtyToAdd);
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
            // kafkaTemplate.send(KAFKA_TOPIC, savedInventory);
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
    public void consumeUserOrderKafkaTopic(String userOrdersAsString) {
        try {
            UserOrders userOrders = objectMapper.readValue(userOrdersAsString, UserOrders.class);
            log.info("User Order received -> {}", userOrders);
            int availableQty = inventoryRepo.getAvailableQty(userOrders.getProdId());


            if (userOrders.getProdQty() < availableQty) {
                inventoryStockStatusService.sendProductToLogistics(userOrders);
            } else {
                int requiredQty = userOrders.getProdQty() - availableQty;
                Long orderID = userOrders.getUserOrderId();
                log.info("Order ID @ Inventory Service = {}", orderID);
                supplierStockAddingService.sendRequestToSupplier(userOrders.getProdId(), requiredQty, orderID);
            }
            // Delete the record from UserCart after processing
            deleteUserCartRecord(userOrders.getUserId(), userOrders.getProdId());

        } catch (Exception e) {
            log.error("Error", e);
        }
    }


    private void deleteUserCartRecord(Long userId, Long prodId) {
        // Use UserCartRepository to delete the record
        UserCart userCart = userCartRepository.findByUserIdAndProdId(userId, prodId);
        if (userCart != null) {
            userCartRepository.delete(userCart);
            log.info("UserCart record deleted for userId: {} and prodId: {}", userId, prodId);
        } else {
            log.warn("UserCart record not found for userId: {} and prodId: {}", userId, prodId);
        }
    }

}
package com.supply.inventoryservice.service;

import com.scm.UserOrder;
import com.supply.inventoryservice.entity.Inventory;
import com.supply.inventoryservice.repo.InventoryRepo;
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
    private final KafkaTemplate<String, Inventory> kafkaTemplate;
    private static final String KAFKA_TOPIC = "inventory";

    @Autowired
    public InventoryService(InventoryRepo inventoryRepo, OrderFulfillmentService inventoryStockStatusService, KafkaTemplate<String, Inventory> kafkaTemplate) {
        this.inventoryRepo = inventoryRepo;
        this.inventoryStockStatusService = inventoryStockStatusService;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Inventory saveInventory(Inventory inventory) {
        Inventory savedInventory = inventoryRepo.save(inventory);

        // Publish the saved inventory to the Kafka topic
        kafkaTemplate.send(KAFKA_TOPIC, savedInventory);

        return savedInventory;
    }

    public List<Inventory> saveInventoryList(List<Inventory> inventoryList) {
        List<Inventory> savedInventoryList = new ArrayList<>();
        for (Inventory inventory : inventoryList) {
            Inventory savedInventory = inventoryRepo.save(inventory);
            savedInventoryList.add(savedInventory);

            // Publish the saved inventory entry to Kafka
            kafkaTemplate.send(KAFKA_TOPIC, savedInventory);
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

        // Trigger the InventoryStockStatusService
        String availability = inventoryStockStatusService.checkProductAvailability(order);

        // Send the product availability message
        inventoryStockStatusService.sendProductToLogistics(order, availability);
    }
}

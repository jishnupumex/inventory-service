package com.supply.inventoryservice.service;

import com.supply.inventoryservice.entity.Inventory;
import com.supply.inventoryservice.repo.InventoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.kafka.core.KafkaTemplate;

@Service
public class InventoryService {

    private final InventoryRepo inventoryRepo;
    private final KafkaTemplate<String, Inventory> kafkaTemplate;
    private static final String KAFKA_TOPIC = "inventory"; // Replace with your Kafka topic name

    @Autowired
    public InventoryService(InventoryRepo inventoryRepo, KafkaTemplate<String, Inventory> kafkaTemplate) {
        this.inventoryRepo = inventoryRepo;
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
}

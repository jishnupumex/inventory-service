package com.supply.inventoryservice.service;

import com.supply.inventoryservice.entity.Inventory;
import com.supply.inventoryservice.repo.InventoryRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.scm.UserOrder;

@Service
public class OrderFulfillmentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderFulfillmentService.class);

    private KafkaTemplate<String, String> kafkaTemplate;
    private InventoryRepo inventoryRepo;

    public OrderFulfillmentService(InventoryRepo inventoryRepo, KafkaTemplate<String, String> kafkaTemplate) {
        this.inventoryRepo = inventoryRepo;
        this.kafkaTemplate = kafkaTemplate;
    }

    public String checkProductAvailability(UserOrder userOrder) {
        int prodId = userOrder.getProdId();
        int userOrderQty = userOrder.getProdQty();

        // Retrieve inventory information for the product
        Inventory inventoryItem = inventoryRepo.findById((long) prodId).orElse(null);

        if (inventoryItem != null) {
            int inventoryQty = inventoryItem.getProdQty();

            if (inventoryQty >= userOrderQty) {
                return "Available";
            }
        }
        return "Not Available";
    }

    public void sendProductToLogistics(UserOrder userOrder, String availability) {
        String message = "Product " + userOrder.getProdName() + " is " + availability;
        kafkaTemplate.send("OrderFulfillment", message);
        LOGGER.info("Sending order fulfillment message to logistics service: {}", message);
    }
}

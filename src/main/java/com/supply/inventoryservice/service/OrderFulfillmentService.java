package com.supply.inventoryservice.service;

import com.supply.inventoryservice.entity.Inventory;
import com.supply.inventoryservice.entity.OrderStatus;
import com.supply.inventoryservice.entity.UserOrders;
import com.supply.inventoryservice.repo.InventoryRepo;
import com.supply.inventoryservice.repo.UserOrdersRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.scm.UserOrder;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderFulfillmentService {

    private final KafkaTemplate<Object, UserOrders> kafkaTemplate;
    private final InventoryRepo inventoryRepo;
    private final UserOrdersRepo userOrdersRepo;
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

    public void sendProductToLogistics(UserOrders userOrder) {
        userOrder.setOrderStatus(OrderStatus.AVAILABLE);
        UserOrders savedUserOrder = userOrdersRepo.save(userOrder);
        Optional<Inventory> optionalInventory = inventoryRepo.findById(userOrder.getProdId());

        if (optionalInventory.isPresent()) {
            Inventory existingInventory = optionalInventory.get();
            // Update the prodQty with the new value
            existingInventory.setProdQty(existingInventory.getProdQty() - userOrder.getProdQty());
            // Save the updated Inventory entry
            inventoryRepo.save(existingInventory);

            // Produce Kafka topic to logistics
            kafkaTemplate.send("OrderFulfillment", savedUserOrder);
            log.info("Sending order fulfillment message to logistics service: {}", savedUserOrder);
            log.info("Order ID : {}", savedUserOrder.getUserOrderId());
        } else {
            log.error("Inventory not found for prodId: {}", userOrder.getProdId());
        }
    }
}

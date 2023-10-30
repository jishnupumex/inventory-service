package com.supply.inventoryservice.controller;

import com.supply.inventoryservice.entity.Inventory;
import com.supply.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/inventories")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8000")
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<Inventory> saveInventory(@RequestBody Inventory inventory) {
        Inventory savedInventory = inventoryService.saveInventory(inventory);
        return new ResponseEntity<>(savedInventory, HttpStatus.CREATED);
    }

    @PostMapping("/save-multiple-records")
    public ResponseEntity<List<Inventory>> saveInventory(@RequestBody List<Inventory> inventoryList) {
        List<Inventory> savedInventoryList = inventoryService.saveInventoryList(inventoryList); // Implement this method in your service
        return new ResponseEntity<>(savedInventoryList, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable Long id) {
        Optional<Inventory> inventory = inventoryService.findInventoryById(id);
        return inventory.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventory() {
        List<Inventory> inventoryList = inventoryService.findAllInventory();
        return new ResponseEntity<>(inventoryList, HttpStatus.OK);
    }
}

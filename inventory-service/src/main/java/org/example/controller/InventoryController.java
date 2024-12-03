package org.example.controller;

import org.example.model.InventoryStatus;
import org.example.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/availability")
public class InventoryController {

    @Autowired
    private InventoryRepository inventoryRepository;

    @GetMapping
    public ResponseEntity<List<InventoryStatus>> getAvailability(@RequestParam List<String> uniqIds) {
        simulateDelay();
        List<InventoryStatus> availabilityList = inventoryRepository.getAvailabilityByUniqIds(uniqIds);
        return ResponseEntity.ok(availabilityList);
    }
    private void simulateDelay() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread was interrupted during delay simulation", e);
        }
    }
}

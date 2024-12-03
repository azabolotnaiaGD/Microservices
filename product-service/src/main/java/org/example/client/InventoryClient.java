package org.example.client;

import org.example.model.InventoryStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "inventory-service", path = "/availability")
public interface InventoryClient {

    @GetMapping
    List<InventoryStatus> getAvailability(@RequestParam("uniqIds") List<String> uniqIds);

}

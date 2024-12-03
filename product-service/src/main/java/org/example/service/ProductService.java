package org.example.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.example.client.CatalogClient;
import org.example.client.InventoryClient;
import org.example.model.InventoryStatus;
import org.example.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private CatalogClient catalogClient;

    @Autowired
    private InventoryClient inventoryClient;

    @CircuitBreaker(name = "catalogService", fallbackMethod = "fallbackForGetProductById")
    public Product getProductByIdIfAvailable(String uniqId) {
        Product product = catalogClient.getProductById(uniqId);
        if (product == null) return null;

        InventoryStatus status = getInventoryAvailability(List.of(uniqId)).stream().findFirst().orElse(null);
        return (status != null && status.isAvailable()) ? product : null;
    }

    @CircuitBreaker(name = "catalogService", fallbackMethod = "fallbackForGetProductsBySku")
    public List<Product> getProductsBySkuIfAvailable(String sku) {
        List<Product> products = catalogClient.getProductsBySku(sku);
        if (products.isEmpty()) return List.of();

        List<String> uniqIds = products.stream().map(Product::getUniqId).collect(Collectors.toList());
        Set<String> availableUniqIds = getInventoryAvailability(uniqIds).stream()
                .filter(InventoryStatus::isAvailable)
                .map(InventoryStatus::getUniqId)
                .collect(Collectors.toSet());

        return products.stream()
                .filter(product -> availableUniqIds.contains(product.getUniqId()))
                .collect(Collectors.toList());
    }

    @CircuitBreaker(name = "inventoryService", fallbackMethod = "fallbackForInventoryAvailability")
    public List<InventoryStatus> getInventoryAvailability(List<String> uniqIds) {
        return inventoryClient.getAvailability(uniqIds);
    }

    private Product fallbackForGetProductById(String uniqId, Throwable throwable) {
        System.err.println("Catalog Service fallback triggered for getProductById: " + uniqId + ", error: " + throwable.getMessage());
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Catalog Service is unavailable. Please try again later.");
    }

    private List<Product> fallbackForGetProductsBySku(String sku, Throwable throwable) {
        System.err.println("Catalog Service fallback triggered for getProductsBySku: " + sku + ", error: " + throwable.getMessage());
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Catalog Service is unavailable. Please try again later.");
    }

    private List<InventoryStatus> fallbackForInventoryAvailability(List<String> uniqIds, Throwable throwable) {
        System.err.println("Inventory Service fallback triggered for uniqIds: " + uniqIds + ", error: " + throwable.getMessage());
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Inventory Service is unavailable. Please try again later.");
    }
}

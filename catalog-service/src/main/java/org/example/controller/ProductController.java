package org.example.controller;

import org.example.model.Product;
import org.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/catalog/products")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/{uniqId}")
    public ResponseEntity<Product> getProductById(@PathVariable String uniqId) {
        simulateDelay();
        Product product = productRepository.getProductById(uniqId);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<List<Product>> getProductsBySku(@PathVariable String sku) {
        List<Product> products = productRepository.getProductsBySku(sku);
        return !products.isEmpty() ? ResponseEntity.ok(products) : ResponseEntity.notFound().build();
    }
    private void simulateDelay() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread was interrupted during delay simulation", e);
        }
    }
}

package org.example.controller;

import org.example.model.Product;
import org.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/{uniqId}")
    public ResponseEntity<Product> getProductById(@PathVariable String uniqId) {
        Product product = productService.getProductByIdIfAvailable(uniqId);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<List<Product>> getProductsBySku(@PathVariable String sku) {
        List<Product> products = productService.getProductsBySkuIfAvailable(sku);
        return products.isEmpty() ? ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build() : ResponseEntity.ok(products);
    }
}

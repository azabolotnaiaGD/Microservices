package org.example.client;


import org.example.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "catalog-service", path = "/catalog/products")
public interface CatalogClient {

    @GetMapping("/{uniqId}")
    Product getProductById(@PathVariable("uniqId") String uniqId);

    @GetMapping("/sku/{sku}")
    List<Product> getProductsBySku(@PathVariable("sku") String sku);

}

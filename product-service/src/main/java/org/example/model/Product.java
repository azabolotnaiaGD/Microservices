package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private String uniqId;
    private String sku;
    private String nameTitle;
    private String description;
    private Double listPrice;
    private Double salePrice;
    private String category;
    private String categoryTree;
    private String averageProductRating;
    private String productUrl;
    private String productImageUrls;
    private String brand;
    private Integer totalNumberReviews;
    private String reviews;

    public Product(String id, String name, String description) {
        this.uniqId = id;
        this.nameTitle = name;
        this.description = description;
    }
}

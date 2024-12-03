package org.example.model;

import lombok.Data;

@Data
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

}

package org.example.repository;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import jakarta.annotation.PostConstruct;
import org.example.model.Product;
import org.springframework.stereotype.Repository;

import java.io.InputStreamReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ProductRepository {
    private final Map<String, Product> productMap = new HashMap<>();

    @PostConstruct
    public void loadProducts() {
        try (CSVReader reader = new CSVReader(new InputStreamReader(
                getClass().getResourceAsStream("/jcpenney_com-ecommerce_sample.csv")))) {
            List<String[]> rows = reader.readAll();
            for (String[] row : rows.subList(1, rows.size())) {
                Product product = new Product();
                product.setUniqId(row[0]);
                product.setSku(row[1]);
                product.setNameTitle(row[2]);
                product.setDescription(row[3]);
                product.setListPrice(parseDouble(row[4]));
                product.setSalePrice(parseDouble(row[5]));
                product.setCategory(row[6]);
                product.setCategoryTree(row[7]);
                product.setAverageProductRating(row[8]);
                product.setProductUrl(row[9]);
                product.setProductImageUrls(row[10]);
                product.setBrand(row[11]);
                product.setTotalNumberReviews(parseInteger(row[12]));
                product.setReviews(row[13]);

                productMap.put(product.getUniqId(), product);
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    private Double parseDouble(String value) {
        try {
            return value != null ? Double.parseDouble(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer parseInteger(String value) {
        try {
            return value != null ? Integer.parseInt(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Product getProductById(String uniqId) {
        return productMap.get(uniqId);
    }

    public List<Product> getProductsBySku(String sku) {
        return productMap.values().stream()
                .filter(product -> sku.equals(product.getSku()))
                .collect(Collectors.toList());
    }
}

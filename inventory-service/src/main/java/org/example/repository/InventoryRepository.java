package org.example.repository;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import jakarta.annotation.PostConstruct;
import org.example.model.InventoryStatus;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Repository
public class InventoryRepository {
    private final Map<String, InventoryStatus> availabilityMap = new HashMap<>();
    private final Random random = new Random();

    @PostConstruct
    public void loadAndGenerateAvailability() {
        try (CSVReader reader = new CSVReader(new InputStreamReader(
                getClass().getResourceAsStream("/jcpenney_com-ecommerce_sample.csv")))) {
            List<String[]> rows = reader.readAll();
            for (String[] row : rows.subList(1, rows.size())) {
                String uniqId = row[0];
                boolean isAvailable = random.nextBoolean();
                availabilityMap.put(uniqId, new InventoryStatus(uniqId, isAvailable));
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    public List<InventoryStatus> getAvailabilityByUniqIds(List<String> uniqIds) {
        return uniqIds.stream()
                .map(id -> availabilityMap.getOrDefault(id, new InventoryStatus(id, false)))
                .collect(Collectors.toList());
    }
}

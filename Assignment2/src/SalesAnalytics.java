package Assignment2.src;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Core analytical engine.
 */
public class SalesAnalytics {

    /**
     * Calculates total revenue across all records.
     * CONCEPT: Stream Reduction
     */
    public double getTotalRevenue(List<SalesRecord> records) {
        return records.stream() // Convert list to Stream
                .mapToDouble(SalesRecord::getLineRevenue) // Map object to double (Lambda: Reference Method)
                .sum(); // Terminal operation: Aggregation
    }

    /**
     * Groups sales by Item and sums revenue.
     * CONCEPT: Grouping & Downstream Collection
     */
    public Map<String, Double> getRevenueByItem(List<SalesRecord> records) {
        return records.stream()
                .collect(Collectors.groupingBy(
                        SalesRecord::getItem, // Classifier
                        Collectors.summingDouble(SalesRecord::getLineRevenue) // Downstream Collector
                ));
    }

    /**
     * Counts orders per customer using their email.
     * CONCEPT: Counting Aggregation
     */
    public Map<String, Long> getOrdersByCustomer(List<SalesRecord> records) {
        return records.stream()
                .collect(Collectors.groupingBy(
                        SalesRecord::getEmailAddress,
                        Collectors.counting() // Functional aggregation: Count elements in group
                ));
    }

    /**
     * Groups sales by Date.
     * CONCEPT: Time-series Grouping
     */
    public Map<LocalDate, Double> getRevenueByDate(List<SalesRecord> records) {
        return records.stream()
                .collect(Collectors.groupingBy(
                        SalesRecord::getOrderDate,
                        Collectors.summingDouble(SalesRecord::getLineRevenue)
                ));
    }

    /**
     * Finds the item with the maximum quantity sold.
     * CONCEPT: Stream Processing Pipeline (Group -> EntrySet -> Stream -> Max)
     */
    public Optional<Map.Entry<String, Integer>> getMostSoldItem(List<SalesRecord> records) {
        // Step 1: Group by Item and Sum Quantities
        Map<String, Integer> quantityByItem = records.stream()
                .collect(Collectors.groupingBy(
                        SalesRecord::getItem,
                        Collectors.summingInt(SalesRecord::getQuantity)
                ));

        // Step 2: Stream the map entries to find the Max value
        return quantityByItem.entrySet().stream()
                .max(Map.Entry.comparingByValue()); // Lambda comparator
    }

    /**
     * Sums total tax.
     * CONCEPT: Simple Aggregation
     */
    public double getTotalTaxCollected(List<SalesRecord> records) {
        return records.stream()
                .mapToDouble(SalesRecord::getTaxAmount)
                .sum();
    }
}
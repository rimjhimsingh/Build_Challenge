package Assignment2.src;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        String filePath = "data/sales.csv";

        // 1. Data Loading Phase
        SalesDataLoader loader = new SalesDataLoader();
        List<SalesRecord> records = loader.load(filePath);

        if (records.isEmpty()) {
            System.out.println("No sales records found. Please check the CSV file path: " + filePath);
            return;
        }

        // 2. Data Analysis Phase
        SalesAnalytics analytics = new SalesAnalytics();
        System.out.println("=== Sales Analysis Report ===\n");

        // Data Aggregation (Summing Revenue)
        double totalRevenue = analytics.getTotalRevenue(records);
        System.out.printf("Total Revenue:       $%.2f%n", totalRevenue);

        // Data Aggregation (Summing Tax)
        double totalTax = analytics.getTotalTaxCollected(records);
        System.out.printf("Total Tax Collected: $%.2f%n", totalTax);

        // Grouping & Finding Max (Functional Operation)
        Optional<Map.Entry<String, Integer>> mostSold = analytics.getMostSoldItem(records);
        if (mostSold.isPresent()) {
            System.out.println("Most Sold Item:      " + mostSold.get().getKey() +
                               " (Total Qty: " + mostSold.get().getValue() + ")");
        } else {
            System.out.println("Most Sold Item:      N/A");
        }

        System.out.println("\n=== End of Report ===");
    }
}
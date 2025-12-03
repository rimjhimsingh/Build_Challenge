package Assignment2.test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import Assignment2.src.SalesAnalytics;
import Assignment2.src.SalesRecord;

public class SalesAnalyticsManualTest {

    // --- Shared Sample Data ---
    private static SalesRecord r1 = new SalesRecord(
            "SO1", 1, LocalDate.of(2019, 7, 1), "Alex", "alex@example.com", "Road Bike", 1, 100.0, 5.0);

    private static SalesRecord r2 = new SalesRecord(
            "SO2", 1, LocalDate.of(2019, 7, 1), "Alex", "alex@example.com", "Helmet", 2, 50.0, 3.0);

    private static SalesRecord r3 = new SalesRecord(
            "SO3", 1, LocalDate.of(2019, 7, 2), "Maria", "maria@example.com", "Road Bike", 1, 100.0, 5.0);

    private static List<SalesRecord> sample = Arrays.asList(r1, r2, r3);

    public static void main(String[] args) {
        SalesAnalytics analytics = new SalesAnalytics();

        System.out.println("=== Running Manual Test Suite ===\n");

        // 1. Standard Happy Path Tests
        testTotalRevenue(analytics);
        testRevenueByItem(analytics);
        testOrdersByCustomer(analytics);
        testRevenueByDate(analytics);
        testMostSoldItem(analytics);
        testTotalTaxCollected(analytics);

        // 2. Edge Case Tests
        testEmptyDataset(analytics);
        testSingleRecord(analytics);
        testRefundRecord(analytics);

        System.out.println("\n=== All Tests Completed ===");
    }

    // ------------------------------------------------------------------------
    // Standard Tests
    // ------------------------------------------------------------------------

    private static void testTotalRevenue(SalesAnalytics analytics) {
        double expected = 100.0 + (2 * 50.0) + 100.0; // 300.0
        double actual = analytics.getTotalRevenue(sample);
        printResult("Total Revenue (Standard)", expected == actual);
    }

    private static void testRevenueByItem(SalesAnalytics analytics) {
        Map<String, Double> result = analytics.getRevenueByItem(sample);
        boolean check1 = result.get("Road Bike") == 200.0;
        boolean check2 = result.get("Helmet") == 100.0;
        printResult("Revenue By Item (Standard)", check1 && check2);
    }

    private static void testOrdersByCustomer(SalesAnalytics analytics) {
        Map<String, Long> result = analytics.getOrdersByCustomer(sample);
        boolean check1 = result.get("alex@example.com") == 2;
        boolean check2 = result.get("maria@example.com") == 1;
        printResult("Orders By Customer (Standard)", check1 && check2);
    }

    private static void testRevenueByDate(SalesAnalytics analytics) {
        Map<LocalDate, Double> result = analytics.getRevenueByDate(sample);
        boolean check1 = result.get(LocalDate.of(2019, 7, 1)) == 200.0;
        boolean check2 = result.get(LocalDate.of(2019, 7, 2)) == 100.0;
        printResult("Revenue By Date (Standard)", check1 && check2);
    }

    private static void testMostSoldItem(SalesAnalytics analytics) {
        Optional<Map.Entry<String, Integer>> result = analytics.getMostSoldItem(sample);
        // Tie handling: Both items have qty 2
        boolean ok = result.isPresent()
                && (result.get().getKey().equals("Helmet") || result.get().getKey().equals("Road Bike"))
                && result.get().getValue() == 2;
        printResult("Most Sold Item (Standard)", ok);
    }

    private static void testTotalTaxCollected(SalesAnalytics analytics) {
        double expected = 5.0 + 3.0 + 5.0; // 13.0
        double actual = analytics.getTotalTaxCollected(sample);
        printResult("Total Tax (Standard)", expected == actual);
    }

    // ------------------------------------------------------------------------
    // Edge Case Tests
    // ------------------------------------------------------------------------

    private static void testEmptyDataset(SalesAnalytics analytics) {
        List<SalesRecord> emptyList = Collections.emptyList();

        boolean checkRevenue = analytics.getTotalRevenue(emptyList) == 0.0;
        boolean checkItemMap = analytics.getRevenueByItem(emptyList).isEmpty();
        boolean checkMostSold = !analytics.getMostSoldItem(emptyList).isPresent();

        printResult("Empty Dataset Handling", checkRevenue && checkItemMap && checkMostSold);
    }

    private static void testSingleRecord(SalesAnalytics analytics) {
        List<SalesRecord> singleList = Collections.singletonList(r1);

        boolean checkRevenue = analytics.getTotalRevenue(singleList) == 100.0;
        boolean checkCount = analytics.getOrdersByCustomer(singleList).get("alex@example.com") == 1;

        printResult("Single Record Handling", checkRevenue && checkCount);
    }

    private static void testRefundRecord(SalesAnalytics analytics) {
        // Create a refund record (negative quantity/price depending on business logic)
        // Assuming refund is represented by negative price here for simplicity
        SalesRecord refund = new SalesRecord(
                "SO4", 1, LocalDate.of(2019, 7, 3), "John", "john@test.com", "Refunded Item", 1, -50.0, 0.0);

        List<SalesRecord> refundList = new ArrayList<>(sample);
        refundList.add(refund);

        double expectedRevenue = 300.0 - 50.0; // 250.0
        double actualRevenue = analytics.getTotalRevenue(refundList);

        printResult("Refund/Negative Value Handling", expectedRevenue == actualRevenue);
    }

    // ------------------------------------------------------------------------
    // Helper
    // ------------------------------------------------------------------------

    private static void printResult(String testName, boolean passed) {
        // Align output for readability
        String status = passed ? "PASS" : "FAIL";
        System.out.printf("%-35s : %s%n", testName, status);
    }
}
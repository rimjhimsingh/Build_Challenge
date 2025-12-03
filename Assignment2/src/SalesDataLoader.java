package Assignment2.src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles data ingestion from CSV files.
 * Parses raw text lines into strongly-typed SalesRecord objects.
 */
public class SalesDataLoader {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public List<SalesRecord> load(String filePath) {
        List<SalesRecord> records = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String header = reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> fields = parseCsvLine(line);

                // Validation: Ensure row has enough columns
                if (fields.size() < 9) {
                    continue; 
                }

                // Map CSV fields to Domain Object
                // Trimming ensures clean data ingestion
                String orderNumber = fields.get(0).trim();
                int lineNumber = Integer.parseInt(fields.get(1).trim());
                LocalDate orderDate = LocalDate.parse(fields.get(2).trim(), DATE_FORMAT);
                String customerName = fields.get(3).trim();
                String email = fields.get(4).trim();
                String item = fields.get(5).trim();
                int quantity = Integer.parseInt(fields.get(6).trim());
                double unitPrice = Double.parseDouble(fields.get(7).trim());
                double taxAmount = Double.parseDouble(fields.get(8).trim());

                records.add(new SalesRecord(
                        orderNumber, lineNumber, orderDate, customerName, 
                        email, item, quantity, unitPrice, taxAmount
                ));
            }
        } catch (IOException e) {
            System.err.println("Error loading CSV file: " + e.getMessage());
        }
        return records;
    }

    /**
     * Custom CSV parser to handle commas inside quoted strings.
     * Example: "Doe, John" should be one field, not two.
     */
    private List<String> parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean insideQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                insideQuotes = !insideQuotes; // Toggle state
            } else if (c == ',' && !insideQuotes) {
                fields.add(current.toString()); // End of field
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        fields.add(current.toString()); // Add last field
        return fields;
    }
}
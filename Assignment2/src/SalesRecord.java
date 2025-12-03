package Assignment2.src;

import java.time.LocalDate;

/**
 * Immutable Data Class representing a single sales transaction.
 */
public class SalesRecord {

    private final String salesOrderNumber;
    private final int lineNumber;
    private final LocalDate orderDate;
    private final String customerName;
    private final String emailAddress;
    private final String item;
    private final int quantity;
    private final double unitPrice;
    private final double taxAmount;

    public SalesRecord(String salesOrderNumber, int lineNumber, LocalDate orderDate, 
                       String customerName, String emailAddress, String item, 
                       int quantity, double unitPrice, double taxAmount) {
        this.salesOrderNumber = salesOrderNumber;
        this.lineNumber = lineNumber;
        this.orderDate = orderDate;
        this.customerName = customerName;
        this.emailAddress = emailAddress;
        this.item = item;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.taxAmount = taxAmount;
    }

    public String getSalesOrderNumber() { return salesOrderNumber; }
    public int getLineNumber() { return lineNumber; }
    public LocalDate getOrderDate() { return orderDate; }
    public String getCustomerName() { return customerName; }
    public String getEmailAddress() { return emailAddress; }
    public String getItem() { return item; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }
    public double getTaxAmount() { return taxAmount; }

    // Helper to calculate revenue for this specific line item
    public double getLineRevenue() { return quantity * unitPrice; }

    @Override
    public String toString() {
        return salesOrderNumber + " " + item + " " + quantity;
    }
}
# Sales Data Analytics (Assignment 2)

## Project Overview

This Java application analyzes a retail sales dataset to generate business insights (revenue, tax, popularity). It is built using **Functional Programming** paradigms, relying heavily on **Java Streams** and **Lambda expressions** to process data efficiently without mutable state or traditional loops.

## Objectives

The core goals of this project are to demonstrate:

- **Data Ingestion:** Parsing CSV files into Java objects.
- **Functional Programming:** Using `Stream` API for stateless logic.
- **Aggregation:** Grouping data (by date, item, customer) and calculating sums/counts.
- **Lambdas:** Writing concise, readable code blocks.

## Data Structure

The application reads from `data/sales.csv`.
**Assumed Columns:**

1.  **Order Info:** Order ID, Line Number, Date
2.  **Customer:** Name, Email (Unique Identifier)
3.  **Product:** Item Name, Quantity, Unit Price
4.  **Financial:** Tax Amount

_Example Row:_
`"SO123", 1, "2024-01-01", "John Doe", "john@email.com", "Widget A", 5, 10.00, 2.50`

## How It Works

1.  **Loader (`SalesDataLoader`):** Reads the CSV line-by-line, handles quoted strings (e.g., "Item, Name"), and maps valid rows to `SalesRecord` objects.
2.  **Analyzer (`SalesAnalytics`):** Takes the list of records and uses Streams to:
    - **Sum** total revenue and tax.
    - **Group** sales by Item and Date.
    - **Count** orders per unique Customer Email.
    - **Find Max** (Most sold item) using comparators.
3.  **Report (`Main`):** Outputs a summary to the console.

## Setup & Execution

**Prerequisites:** Java JDK 8 or higher.

**1. Compile**
Run this from the root `Assignment2` folder. This command compiles both source and test files and outputs them into an `out` directory to keep the project clean.

```bash
javac -d out src/*.java test/*.java
```

**2. Run Main Application**
Executes the main analysis:

```bash
java -cp out Assignment2.src.Main
```

**3. Run Tests**
Executes the manual test suite:

```bash
java -cp out Assignment2.test.SalesAnalyticsManualTest
```

## Design Choices & Assumptions

- **Stream API:** Used exclusively for all calculations to ensure thread-safety potential and code conciseness.
- **Error Handling:** Malformed CSV rows are skipped/logged rather than crashing the entire application.
- **Data Types:** Monetary values are handled as `double` for simplicity in this academic context (Standard industry practice would prefer `BigDecimal`).
- **Customer ID:** `Email` is used as the unique key to identify customers.

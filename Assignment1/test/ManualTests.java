package Assignment1.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

import Assignment1.src.Consumer;
import Assignment1.src.DataItem;
import Assignment1.src.Producer;

/**
 * Comprehensive manual test suite for Producer-Consumer system.
 * No JUnit required. Run as a standalone Java application.
 */
public class ManualTests {

    public static void main(String[] args) {

        System.out.println("Running Manual Tests...\n");

        runTest("Test Data Integrity", ManualTests::testDataIntegrity);
        runTest("Test Producer Blocking on Full Queue", ManualTests::testProducerBlocking);
        runTest("Test Consumer Blocking on Empty Queue", ManualTests::testConsumerBlocking);
        runTest("Test Stop Item Produced", ManualTests::testStopItemInsertion);
        runTest("Test Consumer Stops on Stop Item", ManualTests::testConsumerStop);
        runTest("Test Producer Terminates Cleanly", ManualTests::testProducerTermination);
        runTest("Stress Test with High Load and Small Queue", ManualTests::testStress);

        System.out.println("\nAll tests execution complete.");
    }


    // --------------------------------------------------------------------------
    // Test 1: All produced data must be consumed, in correct order
    // --------------------------------------------------------------------------
    private static void testDataIntegrity() throws Exception {

        int itemCount = 50;
        BlockingQueue<DataItem> queue = new ArrayBlockingQueue<>(10);
        DataItem stopItem = new DataItem(null);

        List<Integer> consumed = Collections.synchronizedList(new ArrayList<>());

        Runnable verifyConsumer = () -> {
            try {
                while (true) {
                    DataItem item = queue.take();
                    if (item == stopItem) break;
                    consumed.add(item.getValue());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        ExecutorService exec = Executors.newFixedThreadPool(2);
        exec.submit(new Producer(queue, itemCount, stopItem));
        exec.submit(verifyConsumer);

        exec.shutdown();
        boolean finished = exec.awaitTermination(20, TimeUnit.SECONDS);

        assertTrue(finished, "Threads should finish within timeout");
        assertEquals(itemCount, consumed.size(), "Consumer should receive all items");

        for (int i = 0; i < itemCount; i++) {
            assertEquals(i + 1, consumed.get(i), "Order mismatch at index " + i);
        }
    }


    // --------------------------------------------------------------------------
    // Test 2: Producer blocks when queue is full
    // --------------------------------------------------------------------------
    private static void testProducerBlocking() throws Exception {

        BlockingQueue<DataItem> queue = new ArrayBlockingQueue<>(1);
        // Fill queue to capacity (1)
        queue.put(new DataItem(1)); 

        Thread producerThread = new Thread(() -> {
            try {
                // Should block here because queue is full
                queue.put(new DataItem(2)); 
            } catch (InterruptedException ignored) {}
        });

        producerThread.start();
        Thread.sleep(500); // Allow time for thread to start and block

        assertTrue(producerThread.isAlive(), "Producer must block when queue is full");

        queue.take(); // Free space

        producerThread.join(1000); // Wait for unblock

        assertTrue(!producerThread.isAlive(), "Producer should finish once space is free");
    }


    // --------------------------------------------------------------------------
    // Test 3: Consumer blocks when queue is empty
    // --------------------------------------------------------------------------
    private static void testConsumerBlocking() throws Exception {

        BlockingQueue<DataItem> queue = new ArrayBlockingQueue<>(1);
        DataItem stopItem = new DataItem(null);

        Thread consumerThread = new Thread(() -> {
            try {
                // Should block here because queue is empty
                queue.take(); 
            } catch (InterruptedException ignored) {}
        });

        consumerThread.start();
        Thread.sleep(500); // Allow time for thread to start and block

        assertTrue(consumerThread.isAlive(), "Consumer must block when queue is empty");

        queue.put(stopItem); // Provide data to unblock

        consumerThread.join(1000);

        assertTrue(!consumerThread.isAlive(), "Consumer should unblock once item arrives");
    }


    // --------------------------------------------------------------------------
    // Test 4: Producer must insert stopItem
    // --------------------------------------------------------------------------
    private static void testStopItemInsertion() throws Exception {

        BlockingQueue<DataItem> queue = new ArrayBlockingQueue<>(5);
        DataItem stopItem = new DataItem(null);

        Thread p = new Thread(new Producer(queue, 3, stopItem));
        p.start();
        p.join(2000);

        assertTrue(queue.contains(stopItem), "Stop item should be inserted by producer");
    }


    // --------------------------------------------------------------------------
    // Test 5: Consumer must stop on stopItem
    // --------------------------------------------------------------------------
    private static void testConsumerStop() throws Exception {

        BlockingQueue<DataItem> queue = new ArrayBlockingQueue<>(5);
        DataItem stopItem = new DataItem(null);

        Thread consumerThread = new Thread(new Consumer(queue, stopItem));
        consumerThread.start();

        queue.put(stopItem);

        consumerThread.join(2000);

        assertTrue(!consumerThread.isAlive(), "Consumer must stop on receiving stopItem");
    }


    // --------------------------------------------------------------------------
    // Test 6: Producer must terminate cleanly
    // --------------------------------------------------------------------------
    private static void testProducerTermination() throws Exception {

        BlockingQueue<DataItem> queue = new ArrayBlockingQueue<>(5);
        DataItem stopItem = new DataItem(null);

        // FIX: Item count (5) must <= Queue Size (5) or it will block without a consumer
        // Producing 5 items + 1 stop item = 6 items. Queue is 5.
        // It will block on the stop item if we do 5. So we produce 4 items + 1 stop item = 5.
        Thread producerThread = new Thread(new Producer(queue, 4, stopItem));
        producerThread.start();

        producerThread.join(5000);

        assertTrue(!producerThread.isAlive(), "Producer must terminate cleanly");
    }


    // --------------------------------------------------------------------------
    // Test 7: Stress test with many items and tiny queue
    // --------------------------------------------------------------------------
    private static void testStress() throws Exception {

        int items = 200;
        // Tiny queue forces frequent context switching
        BlockingQueue<DataItem> queue = new ArrayBlockingQueue<>(2); 
        DataItem stopItem = new DataItem(null);

        Thread p = new Thread(new Producer(queue, items, stopItem));
        Thread c = new Thread(new Consumer(queue, stopItem));

        p.start();
        c.start();

        p.join(8000);
        c.join(8000);

        assertTrue(!p.isAlive(), "Producer must finish in stress test");
        assertTrue(!c.isAlive(), "Consumer must finish in stress test");
    }


    // --------------------------------------------------------------------------
    // Helper assertion and test runner functions
    // --------------------------------------------------------------------------
    private static void runTest(String testName, TestRunnable test) {
        System.out.print(testName + "... ");
        try {
            test.run();
            System.out.println("PASS");
        } catch (Exception e) {
            System.out.println("FAIL");
            System.out.println("  Reason: " + e.getMessage());
        }
    }

    private static void assertTrue(boolean condition, String message) throws Exception {
        if (!condition)
            throw new Exception("Assertion Failed: " + message);
    }

    private static void assertEquals(int expected, int actual, String message) throws Exception {
        if (expected != actual)
            throw new Exception(message + " [Expected " + expected + ", Actual " + actual + "]");
    }

    @FunctionalInterface
    interface TestRunnable {
        void run() throws Exception;
    }
}
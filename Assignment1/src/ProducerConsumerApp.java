package Assignment1.src;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Main application entry point that orchestrates the Producer-Consumer pattern.
 * It sets up the shared resources and manages the thread lifecycle using an ExecutorService.
 */

public class ProducerConsumerApp {

    private static final int QUEUE_SIZE = 5;
    private static final int ITEM_COUNT = 20;

    public static void main(String[] args) {
        // Using a bounded queue determines how much backlog is allowed.
        // If the queue reaches 5 items, the producer will block until the consumer makes space.
        BlockingQueue<DataItem> queue = new ArrayBlockingQueue<>(QUEUE_SIZE);

        // Sentinel object for graceful shutdown (Poison Pill pattern)
        DataItem stopItem = new DataItem(null);

        // Create a thread pool with exactly 2 threads (one for Producer, one for Consumer)
        ExecutorService exec = Executors.newFixedThreadPool(2);

        System.out.println("Starting Producer and Consumer...");
        
        exec.submit(new Producer(queue, ITEM_COUNT, stopItem));
        exec.submit(new Consumer(queue, stopItem));


        // Initiate a graceful shutdown. Previously submitted tasks are executed, 
        // but no new tasks will be accepted.
        exec.shutdown();
        try {
            if (!exec.awaitTermination(15, TimeUnit.SECONDS)) {
                System.err.println("Tasks took too long, forcing shutdown.");
                exec.shutdownNow();
            }
        } catch (InterruptedException ex) {
            exec.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Application finished.");
    }
}
package Assignment1.src;

import java.util.concurrent.BlockingQueue;

/**
 * The Producer class generates data items and places them into a shared BlockingQueue.
 * It implements Runnable to execute on a separate thread.
 */
public class Producer implements Runnable {

    // The shared thread-safe buffer where items are placed
    private final BlockingQueue<DataItem> queue;
    // The number of items to generate before stopping
    private final int count;
    // The special object used to signal the Consumer to terminate (Poison Pill)
    private final DataItem stopItem;

    /**
     * Constructor initializes the producer with the shared queue and production limits.
     * @param queue The queue to store produced items.
     * @param count Total number of items to produce.
     * @param stopItem The specific object instance that acts as the stop signal.
     */
    public Producer(BlockingQueue<DataItem> queue, int count, DataItem stopItem) {
        this.queue = queue;
        this.count = count;
        this.stopItem = stopItem;
    }

    /**
     * Main execution logic for the Producer thread.
     */
    @Override
    public void run() {
        try {
            // Loop to produce the specified number of items
            for (int i = 1; i <= count; i++) {
                DataItem item = new DataItem(i);
                
                // Simulate production time (e.g., reading from DB or heavy calculation)
                Thread.sleep(1); 
                
                System.out.println("[Producer] Generated: " + item);
                // Insert the item into the queue.
                // This call blocks if the queue is full, handling backpressure automatically.
                queue.put(item); // Blocks if queue is full
            }
            
            // After the loop finishes, send the poison pill to signal the consumer to stop
            queue.put(stopItem);
            System.out.println("[Producer] Stop signal sent.");

        } catch (InterruptedException ex) {
            // Handle thread interruption by restoring the interrupted status
            Thread.currentThread().interrupt();
            System.err.println("[Producer] Interrupted.");
        }
    }
}
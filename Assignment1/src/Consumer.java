package Assignment1.src;
import java.util.concurrent.BlockingQueue;
/**
 * The Consumer class is responsible for taking items from the shared BlockingQueue
 * and processing them. It implements Runnable to run on a separate thread.
 */
public class Consumer implements Runnable {

    private final BlockingQueue<DataItem> queue;
    private final DataItem stopItem;

    public Consumer(BlockingQueue<DataItem> queue, DataItem stopItem) {
        this.queue = queue;
        this.stopItem = stopItem;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Blocks if queue is empty
                DataItem item = queue.take(); 
                
                // Reference equality check for the poison pill
                if (item == stopItem) {
                    System.out.println("[Consumer] Stop signal received.");
                    break;
                }
                
                process(item);
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            System.err.println("[Consumer] Interrupted.");
        }
    }

    private void process(DataItem item) throws InterruptedException {
        // Simulate processing time (e.g., writing to DB)
        Thread.sleep(1); 
        System.out.println("[Consumer] Processed: " + item);
    }
}
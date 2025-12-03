package Assignment1.src;

/**
 * Represents a unit of data to be passed between the Producer and Consumer.
 * This class acts as a simple immutable wrapper around an Integer value.
 */
public class DataItem {
    // The data payload, marked final to make the item immutable once created
    private final Integer value;

    /**
     * Constructor to initialize the data item.
     * @param value The integer value this item holds.
     */
    public DataItem(Integer value) {
        this.value = value;
    }

    /**
     * Retrieves the stored value.
     * @return The Integer payload.
     */
    public Integer getValue() {
        return value;
    }
    
    /**
     * Returns a string representation of the item.
     * Useful for printing logs to the console to track which item is being processed.
     */
    @Override
    public String toString() {
        return "Item-" + value;
    }
}
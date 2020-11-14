package globingular.core;

/**
 * An event to send upon notifying listener.
 * 
 * @param <T> Type of element being added, updated or removed.
 */
public class ChangeEvent<T> implements ListenerEvent<T> {

    /**
     * Enum values for the different change statuses.
     */
    public enum Status {
        /**
         * Use if this ChangeEvent represents an element being added.
         */
        ADDED,
        /**
         * Use if this ChangeEvent represents an element being updated.
         */
        UPDATED,
        /**
         * Use if this ChangeEvent represents an element being removed.
         */
        REMOVED
    }

    /**
     * Contains this ChangeEvents status.
     */
    private Status status;
    /**
     * The element having been changed.
     */
    private T element;

    /**
     * Initialize a new ChangeEvent, with the given status and element.
     * 
     * @param status An enum value representing the different statuses
     * @param element The element having been changed
     */
    public ChangeEvent(final Status status, final T element) {
        this.status = status;
        this.element = element;
    }

    /**
     * Check if the change represents an addition.
     * 
     * @return true if the element was added
     */
    public boolean wasAdded() {
        return this.status == Status.ADDED;
    }

    /**
     * Check if the change represents an update.
     * 
     * @return true if the element was updated
     */
    public boolean wasUpdated() {
        return this.status == Status.UPDATED;
    }

    /**
     * Check if the change represents a removal.
     * 
     * @return true if the element was removed
     */
    public boolean wasRemoved() {
        return this.status == Status.REMOVED;
    }

    /**
     * Get the element part of the change.
     * 
     * @return the element added, updated or removed
     */
    @Override
    public T getElement() {
        return element;
    }
}
